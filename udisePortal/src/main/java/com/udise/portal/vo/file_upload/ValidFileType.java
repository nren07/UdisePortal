package com.udise.portal.vo.file_upload;

import com.udise.portal.common.FileTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface ValidFileType {

    String message() default "File type isn't valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] allowed() default { "pdf", "doc", "docx", "xls", "xlsx", "jpg", "jpeg", "png", "tif", "txt", "csv" };
}