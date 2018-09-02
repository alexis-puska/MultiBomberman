package com.mygdx.dto.sprite;

import java.io.Serializable;

import com.mygdx.enumeration.LouisSpriteEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpriteLouis implements Serializable {

    private static final long serialVersionUID = -8772069448741457644L;

    private int y;
    private int x;
    private int n;
    private int ny;
    private int nx;
    private int sy;
    private int sx;
    private LouisSpriteEnum animation;
    private String grp;
    private String r;
}
