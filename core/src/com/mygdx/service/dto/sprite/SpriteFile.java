package com.mygdx.service.dto.sprite;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpriteFile implements Serializable {

    private static final long serialVersionUID = -8772069448741457644L;

    private String file;
    private List<Sprite> area;

}
