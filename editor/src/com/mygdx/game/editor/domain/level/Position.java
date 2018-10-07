package com.mygdx.game.editor.domain.level;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position implements Serializable {

    private static final long serialVersionUID = -3148349064427411770L;
    private int x;
    private int y;
}
