package com.mygdx.service.network.dto.sync;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.mygdx.domain.enumeration.BonusTypeEnum;
import com.mygdx.domain.game.Bonus;
import com.mygdx.domain.level.Level;
import com.mygdx.service.network.enumeration.NetworkGameRequestEnum;
import com.mygdx.service.network.utils.NetworkRequestUtils;

/**
 * @author Alexis PUSKARCZYK Used for encode string to send position of all
 *         bonus to the client each new second of elapsed time game. Manage the
 *         delta request, revelead bonus, bonus taked, bonus burn. Create to
 *         reduce the network traffic size.
 */
public class BonusPositionDTO {

	private Map<Integer, BonusTypeEnum> reveleadBonusType;

	public BonusPositionDTO(Level level) {
		reveleadBonusType = new HashMap<>();
		level.getBonuss().stream().filter(Bonus::isRevealed).forEach(b -> {
			reveleadBonusType.put(b.getGridIndex(), b.getType());
		});
	}

	public BonusPositionDTO(byte[] encoded) {
		ByteBuffer bb = ByteBuffer.wrap(encoded);
		reveleadBonusType = new HashMap<>();
		int index = 0;
		while (bb.remaining() > 0) {
			BonusTypeEnum b = BonusTypeEnum.values()[(int) bb.get()];

			reveleadBonusType.put(index, b);
			index++;
		}
	}

	public byte[] getBuffer() {
		ByteBuffer bb;
		bb = ByteBuffer.allocate(NetworkGameRequestEnum.BONUS_POSITION.getRequestLenght());
		bb.put((byte) NetworkGameRequestEnum.BONUS_POSITION.ordinal());
		for (int i = 0; i < NetworkRequestUtils.calcBufferSizeForBonus(); i++) {
			if (reveleadBonusType.containsKey(i)) {
				bb.put((byte) reveleadBonusType.get(i).ordinal());
			} else {
				bb.put((byte) BonusTypeEnum.NONE.ordinal());
			}
		}
		return bb.array();
	}

	public void removeBonus(int index) {
		this.reveleadBonusType.remove(index);
	}

	public void bonusTaked(int index) {
		this.reveleadBonusType.remove(index);
	}

	public void bonusAppeared(int index, BonusTypeEnum type) {
		this.reveleadBonusType.put(index, type);
	}

	public BonusTypeEnum get(int index) {
		return reveleadBonusType.containsKey(index) ? reveleadBonusType.get(index) : BonusTypeEnum.NONE;
	}
}
