package com.altersis.skillmatrix.profileRole;

import com.altersis.skillmatrix.enumeration.ProfileRoleName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
public class ProfileRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProfileRole;

    @Enumerated(EnumType.STRING)
    private ProfileRoleName roleName;

    private String description;

    private String displayName;

    @Column(columnDefinition = "TEXT")
    private String categoryMinScores;

    // Getter and setter for categoryMinScores

    public Map<String, Double> getCategoryMinScoresMap() {
        // Deserialize the JSON string to a Map using a JSON library
        // Here, we'll use Jackson as an example
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(categoryMinScores, new TypeReference<Map<String, Double>>() {});
        } catch (IOException e) {
        }
        return new HashMap<>(); // Return an empty map if deserialization fails
    }

    public void setCategoryMinScoresMap(Map<String, Double> categoryMinScores) {
        // Serialize the Map to a JSON string using a JSON library
        // Here, we'll use Jackson as an example
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.categoryMinScores = objectMapper.writeValueAsString(categoryMinScores);
        } catch (JsonProcessingException e) {
        }
    }
}
