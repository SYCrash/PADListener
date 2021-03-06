package fr.neraud.padlistener.model;

import java.io.Serializable;
import java.util.Date;

import fr.neraud.padlistener.pad.constant.StartingColor;

/**
 * FriendModel for captured data
 *
 * @author Neraud
 */
public class CapturedFriendModel implements Serializable {

	private long id;
	private String name;
	private int rank;
	private StartingColor startingColor;
	private Date lastActivityDate;
	private boolean favourite;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public StartingColor getStartingColor() {
		return startingColor;
	}

	public void setStartingColor(StartingColor startingColor) {
		this.startingColor = startingColor;
	}

	public Date getLastActivityDate() {
		return lastActivityDate;
	}

	public void setLastActivityDate(Date lastActivityDate) {
		this.lastActivityDate = lastActivityDate;
	}

	public boolean isFavourite() {
		return favourite;
	}

	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	@Override
	public String toString() {
		return "(" + id + ") " + name;
	}
}
