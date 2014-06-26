package fr.neraud.padlistener.model;

import java.io.Serializable;

import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * Base MonsterModel
 *
 * @author Neraud
 */
public abstract class BaseMonsterModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private long exp;
	private int skillLevel;
	private int plusHp;
	private int plusAtk;
	private int plusRcv;
	private int awakenings;
	private MonsterPriority priority;
	private String note;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getExp() {
		return exp;
	}

	public void setExp(long exp) {
		this.exp = exp;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getPlusHp() {
		return plusHp;
	}

	public void setPlusHp(int plusHp) {
		this.plusHp = plusHp;
	}

	public int getPlusAtk() {
		return plusAtk;
	}

	public void setPlusAtk(int plusAtk) {
		this.plusAtk = plusAtk;
	}

	public int getPlusRcv() {
		return plusRcv;
	}

	public void setPlusRcv(int plusRcv) {
		this.plusRcv = plusRcv;
	}

	public int getAwakenings() {
		return awakenings;
	}

	public void setAwakenings(int awakenings) {
		this.awakenings = awakenings;
	}

	public MonsterPriority getPriority() {
		return priority;
	}

	public void setPriority(MonsterPriority priority) {
		this.priority = priority;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(").append(id).append(") ");
		builder.append(exp).append(" xp, ");
		builder.append(skillLevel).append(" skill, ");
		builder.append("+").append(plusHp);
		builder.append(" ").append(priority);
		builder.append(" +").append(plusAtk);
		builder.append(" +").append(plusRcv);
		builder.append(" ").append(awakenings);
		return builder.toString();
	}
}
