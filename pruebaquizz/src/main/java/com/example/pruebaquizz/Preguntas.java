package com.example.pruebaquizz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import java.util.List;

@Data
public class Preguntas {

     @NotBlank
     private String nombre;

     @NotBlank
     private String apellido;

     @NotNull
     @Min(value = 1)
     @Max(value = 120)
     private Integer edad;

     @NotBlank
     private String genero;

     @NotEmpty
     private List<String> aficion;
}
