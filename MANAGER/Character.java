package MANAGER;

import java.awt.Color;

public class Character {
	String characterName;
	int health;
	int damage;
	Color color;
	public Character(String name,int health,int damage) {
		this.characterName=name;
		this.health=health;
		this.damage=damage;
		this.color=Color.blue;
	}

}
