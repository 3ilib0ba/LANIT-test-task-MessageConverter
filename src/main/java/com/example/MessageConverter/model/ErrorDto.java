package com.example.MessageConverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Objects;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorDto {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("time")
    private LocalDateTime time;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ErrorDto errorDTO = (ErrorDto) o;
        return Objects.equals(this.code, errorDTO.code) &&
                Objects.equals(this.message, errorDTO.message) &&
                Objects.equals(this.time, errorDTO.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, time);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Error {\n");

        sb.append("    code: ").append(toIndentedString(code)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    time: ").append(toIndentedString(time)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
