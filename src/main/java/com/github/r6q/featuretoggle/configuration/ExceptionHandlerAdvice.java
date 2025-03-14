package com.github.r6q.featuretoggle.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.github.r6q.featuretoggle.common.rest.Error;
import com.github.r6q.featuretoggle.common.rest.ErrorResponse;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingMatrixVariableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;

import static com.github.r6q.featuretoggle.common.rest.ErrorCode.CONVERSION;
import static com.github.r6q.featuretoggle.common.rest.ErrorCode.INTERNAL;
import static com.github.r6q.featuretoggle.common.rest.ErrorCode.VALIDATION;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(UnsatisfiedServletRequestParameterException ex) {
        var errors = Arrays.stream(ex.getParamConditions())
                .map((condition) -> Error.of(VALIDATION, "Parameter condition not met", Map.of("condition", condition)))
                .toList();

        return new ErrorResponse(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(ServletRequestBindingException ex) throws ServletRequestBindingException {
        record Details(String attribute, Map<String, String> params) {
        }

        var details = switch (ex) {
            case MissingMatrixVariableException e ->
                    new Details("Matrix variable", Map.of("variable", e.getVariableName()));
            case MissingPathVariableException e ->
                    new Details("URI path variable", Map.of("variable", e.getVariableName()));
            case MissingRequestCookieException e -> new Details("Cookie", Map.of("cookie", e.getCookieName()));
            case MissingRequestHeaderException e -> new Details("Header", Map.of("header", e.getHeaderName()));
            case MissingServletRequestParameterException e ->
                    new Details("Request Param", Map.of("header", e.getParameterName()));
            default -> throw ex;
        };

        return new ErrorResponse(List.of(Error.of(VALIDATION, details.attribute, details.params)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(TypeMismatchException ex) {
        var params = Optional.of(ex)
                .filter(e -> e instanceof MethodArgumentTypeMismatchException)
                .map(e -> Map.of(
                        "field", ((MethodArgumentTypeMismatchException) e).getName(),
                        "type", Optional.ofNullable(e.getRequiredType()).map(Class::getSimpleName).orElse("")
                ))
                .orElse(Map.of());

        return new ErrorResponse(List.of(Error.of(CONVERSION, "Conversion failure", params)));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(HttpMessageConversionException ex) {
        var error = switch (ex.getCause()) {
            case JsonProcessingException cause -> {
                var params = switch (cause) {
                    case MismatchedInputException e -> Map.of(
                            "field", e.getPath().stream()
                                    .map(ref -> Optional.ofNullable(ref.getFieldName()).orElse("[" + ref.getIndex() + "]"))
                                    .collect(Collectors.joining(".")),
                            "type", e.getTargetType().getSimpleName()
                    );
                    default -> Map.<String, String>of();
                };

                yield Error.of(CONVERSION, "Failure parsing json", params);
            }
            default -> Error.of(CONVERSION, "Failure reading body");
        };

        return new ErrorResponse(List.of(error));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations().stream()
                .map(violation -> {
                    var params = StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                            .filter(node -> Set.of(ElementKind.PARAMETER, ElementKind.PROPERTY).contains(node.getKind()))
                            .findFirst()
                            .map(node -> Map.of("field", node.getName()))
                            .orElse(Map.of());

                    return Error.of(VALIDATION, violation.getMessage(), params);
                })
                .toList();

        return new ErrorResponse(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(BindException ex) {
        var errors = ex.getBindingResult().getFieldErrors().stream()
                .map(violation -> Error.of(VALIDATION, violation.getDefaultMessage(), Map.of("param", violation.getField())))
                .toList();
        return new ErrorResponse(errors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse handle(Throwable ex) {
        // TODO logging
        return new ErrorResponse(List.of(Error.of(INTERNAL, ex.getMessage())));
    }
}
