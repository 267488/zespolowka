/*
 * Copyright 2016 Agata Kostrzewa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pz.twojaszkola.galleryStudent;

import pz.twojaszkola.config.DatastoreConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static java.lang.String.format;
import static java.security.MessageDigest.getInstance;
import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;
import pz.twojaszkola.zainteresowania.zainteresowaniaController;

/**
 * @author Agata Kostrzewa
 */
@Controller
class GalleryStudentController {

    @FunctionalInterface
    public interface FilenameGenerator {

        public String generateFile(final String originalFilename);
    }

    private final GalleryStudentRepository galleryStudentRepository;
    private final UserRepository userRepository;
    private final File datastoreBaseDirectory;
    private FilenameGenerator filenameGenerator = originalFilename -> {
        try {
            final byte[] digest = getInstance("MD5").digest(String.format("%s-%d", originalFilename, System.currentTimeMillis()).getBytes());
            return format("%032x.jpg", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    };

    @Autowired
    public GalleryStudentController(GalleryStudentRepository galleryStudentRepository, final File datastoreBaseDirectory, final UserRepository userRepo) {
        this.galleryStudentRepository = galleryStudentRepository;
        this.datastoreBaseDirectory = datastoreBaseDirectory;
        this.userRepository = userRepo;
    }

    @RequestMapping("/api/galleryStudent/{id:\\d+}")
    public @ResponseBody
    GalleryStudentEntity getStudentPictures(final @PathVariable Integer id) {
        return galleryStudentRepository.findOne(id);
    }

    @RequestMapping(value = "/api/galleryStudent", method = POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GalleryStudentEntity> createGalleryStudent(
            @RequestParam("imageData")
            final MultipartFile imageData
    ) {
        ResponseEntity<GalleryStudentEntity> rv;
        if (imageData == null || imageData.isEmpty()) {
            rv = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            final String filename = this.filenameGenerator.generateFile(imageData.getOriginalFilename());
            final File imageFile = new File(datastoreBaseDirectory, String.format("%s/%s", DatastoreConfig.GALLERY_STUDENT_DIRECTORY, filename));

            try (FileOutputStream out = new FileOutputStream(imageFile);) {
                out.getChannel().transferFrom(Channels.newChannel(imageData.getInputStream()), 0, Integer.MAX_VALUE);
                out.flush();
                CurrentUser currentUser = null;
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                currentUser = (CurrentUser) auth.getPrincipal();
                Integer idUsera = currentUser.getId();
                
                User user = userRepository.findById(idUsera);
                GalleryStudentEntity galleryStudent = new GalleryStudentEntity(user, filename);

                rv = new ResponseEntity<>(this.galleryStudentRepository.save(galleryStudent), HttpStatus.OK);
            } catch (IOException e) {
                // Could not store data...
                rv = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (DataIntegrityViolationException e) {
                rv = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return rv;
    }

    @RequestMapping({"/api/galleryStudent/{id:\\d+}.jpg"})
    public void getGalleryStudent(
            final @PathVariable Integer id,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws IOException {

        GalleryStudentEntity galleryStudent;
        if ((galleryStudent = this.galleryStudentRepository.findOne(id)) == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else {
            final File imageFile = new File(datastoreBaseDirectory, String.format("%s/%s", DatastoreConfig.GALLERY_STUDENT_DIRECTORY, galleryStudent.getFilename()));

            final int cacheForDays = 365;
            response.setHeader("Content-Type", "image/jpeg");
            response.setHeader("Content-Disposition", String.format("inline; filename=\"%s.jpg\"", id));
            response.setHeader("Expires", now(of("UTC")).plusDays(cacheForDays).format(RFC_1123_DATE_TIME));
            response.setHeader("Cache-Control", String.format("max-age=%d, %s", TimeUnit.DAYS.toSeconds(cacheForDays), "public"));

            // Attribute maybe null
            if (request == null || !Boolean.TRUE.equals(request.getAttribute("org.apache.tomcat.sendfile.support"))) {
                Files.copy(imageFile.toPath(), response.getOutputStream());
                response.getOutputStream().flush();
            } else {
                long l = imageFile.length();
                request.setAttribute("org.apache.tomcat.sendfile.filename", imageFile.getAbsolutePath());
                request.setAttribute("org.apache.tomcat.sendfile.start", 0l);
                request.setAttribute("org.apache.tomcat.sendfile.end", l);
                response.setHeader("Content-Length", Long.toString(l));
            }
        }

        response.flushBuffer();
    }
}
