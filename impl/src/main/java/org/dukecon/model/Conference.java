package org.dukecon.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author Niko Köbler, http://www.n-k.de, @dasniko
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Conference {

    private String name;
    private String url;

}
