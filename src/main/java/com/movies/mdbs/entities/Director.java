package com.movies.mdbs.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="directors")
public class Director {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@JsonProperty(access =JsonProperty.Access.READ_ONLY)
private  Long id;


private String name;
private String originalName;

public Director(String name,String originalName){
    this.name = name;
    this.originalName = originalName;

}

}
