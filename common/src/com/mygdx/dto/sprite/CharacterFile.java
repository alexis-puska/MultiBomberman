package com.mygdx.dto.sprite;

import java.io.Serializable;

import com.mygdx.enumeration.CharacterEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterFile implements Serializable {

    private static final long serialVersionUID = -8772069448741457644L;

    private String file;
    private CharacterEnum character;

}
