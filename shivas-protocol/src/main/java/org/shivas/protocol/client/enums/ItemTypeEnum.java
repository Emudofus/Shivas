package org.shivas.protocol.client.enums; /**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 20:03
 * IDE : IntelliJ IDEA
 */

import java.util.HashMap;
import java.util.Map;

public enum ItemTypeEnum {
    Amulet(1),
	Bow(2),
	Wand(3),
	Staff(4),
	Dagger(5),
	Sword(6),
	Hammer(7),
	Shovel(8),
	Ring(9),
	Belt(10),
	Boot(11),
	Potion(12),
	ExperienceParchment(13),
	Gift(14),
	Resource(15),
	Hat(16),
	Cloack(17),
	Pet(18),
	Axe(19),
	Tool(20),
	Pickaxe(21),
	Scythe(22),
	Dofus(23),
	Quest(24),
	Document(25),
	AlchemyPotion(26),
	Transform(27),
	BoostFood(28),
	Benediction(29),
	Malediction(30),
	RolePlayGift(31),
	Follower(32),
	Bread(33),
	Cereal(34),
	Flower(35),
	Plant(36),
	Beer(37),
	Wood(38),
	Ore(39),
	Alloy(40),
	Fish(41),
	Candy(42),
	ForgetPotion(43),
	JobPotion(44),
	SpellPotion(45),
	Fruit(46),
	Bone(47),
	Powder(48),
	PreciousStone(50),
	Stone(51),
	Flour(52),
	Feather(53),
	Hair(54),
	Fabric(55),
	Leather(56),
	Wool(57),
	Seed(58),
	Skin(59),
	Oil(60),
	StuffedToy(61),
	GuttedFish(62),
	Meat(63),
	PreservedMeat(64),
	Tail(65),
	Metaria(66),
	Vegetable(68),
	ComestibleMeat(69),
	Dye(70),
	AlchemyEquipment(71),
	PetEgg(72),
	WeaponControl(73),
	FeeArtifice(74),
	SpellParchment(75),
	StatParchment(76),
	KennelCertificate(77),
	SmithMagicRune(78),
	Drink(79),
	QuestObject(80),
	Backpack(81),
	Shield(82),
	Soulstone(83),
	Key(84),
	FullSoulstone(85),
	PercepteurForgetPotion(86),
	PARCHO_RECHERCHE(87),
	MagicStone(88),
	Gifts(89),
	GhostPet(90),
	Dragodinde(91),
	Bouftou(92),
	BreedingObject(93),
	UsableObject(94),
	Plank(95),
	Bark(96),
	DragodindeCertificate(97),
	Root(98),
	CatchNet(99),
	ResourceBag(100),
	Crossbow(102),
	Paw(103),
	Wing(104),
	Egg(105),
	Ear(106),
	Carapace(107),
	Bud(108),
	Eye(109),
	Jelly(110),
	Shell(111),
	Prism(112),
	Obvijevan(113),
	MagicWeapon(114),
	ShushuSoulPiece(115),
	PetPotion(116);

    private int value;

    private ItemTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
    
    public boolean isEquipment() {
    	return this == Amulet 	||
    		   this == Ring 	||
    		   this == Belt 	||
    		   this == Boot 	||
    		   this == Hat 		||
    		   this == Cloack 	||
    		   this == Pet 		||
    		   this == Dofus 	||
    		   this == Backpack ||
    		   this == Shield 	||
    		  this == Dragodinde||
    		   this == Obvijevan||
    		   this.isWeapon();
    }
    
    public boolean isWeapon() {
    	return this == Bow 		||
    		   this == Wand 	||
    		   this == Staff 	||
    		   this == Dagger 	||
    		   this == Sword 	||
    		   this == Hammer 	||
    		   this == Shovel 	||
    		   this == Axe 		||
    		   this == Tool 	||
    		   this == Pickaxe 	||
    		   this == Scythe 	||
    		   this == Crossbow ||
    		   this == Soulstone||
    		   this == MagicWeapon;
    }
    
    public boolean isUsable() {
    	return  this == Potion 			||
    	     this == ExperienceParchment||
    			this == Gift 			||
    			this == BoostFood 		||
    			this == Benediction 	||
    			this == Malediction 	||
    			this == Bread 			||
    			this == Beer 			||
    			this == Candy 			||
    			this == ForgetPotion 	||
    			this == JobPotion 		||
    			this == SpellPotion 	||
    			this == GuttedFish 		||
    			this == Meat 			||
    			this == PreservedMeat 	||
    			this == ComestibleMeat 	||
    			this == WeaponControl 	||
    			this == FeeArtifice 	||
    			this == SpellParchment 	||
    			this == StatParchment 	||
    			this == Drink 			||
    			this == FullSoulstone 	||
    			this == Gifts 			||
    			this == UsableObject 	||
    			this == CatchNet 		||
    			this == Prism 			||
    			this == PetPotion;
    }
    
    public boolean isResource() {
    	return !isEquipment() || !isUsable();
    }

    private static final Map<Integer, ItemTypeEnum> values = new HashMap<Integer, ItemTypeEnum>();

    static {
        for (ItemTypeEnum e : values()) {
            values.put(e.value(), e);
        }
    }

    public static ItemTypeEnum valueOf(int value) {
        return values.get(value);
    }
}
