package com.mygdx.game.editor.constant;

public enum GameKeyEnum {
	GORDON(117),
	PASSE_PARTOUT(304),
	RIGOR_DANGEROUS(305),
	MELUZZINE(306),
	BOURRU(307),
	FURTOK_GLACIAL(308),
	CLE_ROUILLEE(309),
	PASSE_PARTOUT_BOIS_JOLI(310),
	MONDE_ARDU(311),
	PIQUANTE(312),
	PASSE_TUBERULOZ(313),
	CAUCHEMAR(314),
	PASS_PYRAMIDE(317);
			
	private int itemId;
	
	private GameKeyEnum(int itemId) {
		this.itemId = itemId;
	}
	
	public int getItemId() {
		return itemId;
	}
}
