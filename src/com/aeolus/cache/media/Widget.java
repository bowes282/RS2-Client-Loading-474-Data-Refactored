package com.aeolus.cache.media;
import com.aeolus.Game;
import com.aeolus.cache.def.NpcDefinition;
import com.aeolus.cache.def.ItemDefinition;
import com.aeolus.collection.ReferenceCache;
import com.aeolus.media.font.TextClass;
import com.aeolus.media.font.GameFont;
import com.aeolus.media.renderable.Model;
import com.aeolus.net.Buffer;
import com.aeolus.net.CacheArchive;

/**
 * Previously known as RSInterface, which is a class
 * used to create and show game interfaces.
 */
public final class Widget {
	
	public static final int OPTION_OK = 1;
	public static final int OPTION_USABLE = 2;
	public static final int OPTION_CLOSE = 3;
	public static final int OPTION_TOGGLE_SETTING = 4;
	public static final int OPTION_RESET_SETTING = 5;
	public static final int OPTION_CONTINUE = 6;

	public static final int TYPE_CONTAINER = 0;
	public static final int TYPE_MODEL_LIST = 1;
	public static final int TYPE_INVENTORY = 2;
	public static final int TYPE_RECTANGLE = 3;
	public static final int TYPE_TEXT = 4;
	public static final int TYPE_SPRITE = 5;
	public static final int TYPE_MODEL = 6;
	public static final int TYPE_ITEM_LIST = 7;

	public void swapInventoryItems(int i, int j) {
		int id = inventoryItemId[i];
		inventoryItemId[i] = inventoryItemId[j];
		inventoryItemId[j] = id;
		id = invStackSizes[i];
		invStackSizes[i] = invStackSizes[j];
		invStackSizes[j] = id;
	}

	public static void load(CacheArchive interfaces, GameFont textDrawingAreas[], CacheArchive graphics) {
		spriteCache = new ReferenceCache(50000);
		Buffer buffer = new Buffer(interfaces.getDataForName("data"));
		int defaultParentId = -1;
		buffer.readUShort();
		interfaceCache = new Widget[31000];
		
		while (buffer.currentPosition < buffer.payload.length) {
			int interfaceId = buffer.readUShort();
			if (interfaceId == 65535) {
				defaultParentId = buffer.readUShort();
				interfaceId = buffer.readUShort();
			}
			
			Widget widget = interfaceCache[interfaceId] = new Widget();
			widget.id = interfaceId;
			widget.parent = defaultParentId;
			widget.type = buffer.readUnsignedByte();
			widget.optionType = buffer.readUnsignedByte();
			widget.contentType = buffer.readUShort();
			widget.width = buffer.readUShort();
			widget.height = buffer.readUShort();
			widget.opacity = (byte) buffer.readUnsignedByte();
			widget.hoverType = buffer.readUnsignedByte();
			if (widget.hoverType != 0)
				widget.hoverType = (widget.hoverType - 1 << 8) + buffer.readUnsignedByte();
			else
				widget.hoverType = -1;
			int operators = buffer.readUnsignedByte();
			if (operators > 0) {
				widget.scriptOperators = new int[operators];
				widget.scriptDefaults = new int[operators];
				for (int index = 0; index < operators; index++) {
					widget.scriptOperators[index] = buffer.readUnsignedByte();
					widget.scriptDefaults[index] = buffer.readUShort();
				}

			}
			int scripts = buffer.readUnsignedByte();
			if (scripts > 0) {
				widget.scripts = new int[scripts][];
				for (int script = 0; script < scripts; script++) {
					int instructions = buffer.readUShort();
					widget.scripts[script] = new int[instructions];
					for (int instruction = 0; instruction < instructions; instruction++)
						widget.scripts[script][instruction] = buffer.readUShort();

				}

			}
			if (widget.type == TYPE_CONTAINER) {
				widget.drawsTransparent = false;
				widget.scrollMax = buffer.readUShort();
				widget.hoverOnly = buffer.readUnsignedByte() == 1;
				int i2 = buffer.readUShort();
				widget.children = new int[i2];
				widget.childX = new int[i2];
				widget.childY = new int[i2];
				for (int index = 0; index < i2; index++) {
					widget.children[index] = buffer.readUShort();
					widget.childX[index] = buffer.readShort();
					widget.childY[index] = buffer.readShort();
				}
			}
			if (widget.type == TYPE_MODEL_LIST) {
				buffer.readUShort();
				buffer.readUnsignedByte();
			}
			if (widget.type == TYPE_INVENTORY) {
				widget.inventoryItemId = new int[widget.width * widget.height];
				widget.invStackSizes = new int[widget.width * widget.height];
				widget.aBoolean259 = buffer.readUnsignedByte() == 1;
				widget.hasActions = buffer.readUnsignedByte() == 1;
				widget.usableItems = buffer.readUnsignedByte() == 1;
				widget.replaceItems = buffer.readUnsignedByte() == 1;
				widget.spritePaddingX = buffer.readUnsignedByte();
				widget.spritePaddingY = buffer.readUnsignedByte();
				widget.spritesX = new int[20];
				widget.spritesY = new int[20];
				widget.sprites = new Sprite[20];
				for (int j2 = 0; j2 < 20; j2++) {
					int k3 = buffer.readUnsignedByte();
					if (k3 == 1) {
						widget.spritesX[j2] = buffer.readShort();
						widget.spritesY[j2] = buffer.readShort();
						String s1 = buffer.readString();
						if (graphics != null && s1.length() > 0) {
							int i5 = s1.lastIndexOf(",");
							widget.sprites[j2] = method207(Integer.parseInt(s1.substring(i5 + 1)), graphics, s1.substring(0, i5));
						}
					}
				}
				widget.actions = new String[5];
				for (int actionIndex = 0; actionIndex < 5; actionIndex++) {
					widget.actions[actionIndex] = buffer.readString();
					if (widget.actions[actionIndex].length() == 0)
						widget.actions[actionIndex] = null;
					if (widget.parent == 1644)
						widget.actions[2] = "Operate";
				}
			}
			if (widget.type == TYPE_RECTANGLE)
				widget.filled = buffer.readUnsignedByte() == 1;
			if (widget.type == TYPE_TEXT || widget.type == TYPE_MODEL_LIST) {
				widget.centerText = buffer.readUnsignedByte() == 1;
				int k2 = buffer.readUnsignedByte();
				if (textDrawingAreas != null)
					widget.textDrawingAreas = textDrawingAreas[k2];
				widget.textShadow = buffer.readUnsignedByte() == 1;
			}
			
			if (widget.type == TYPE_TEXT) {
				widget.defaultText = buffer.readString().replaceAll("RuneScape", "Project Aeolus");
				if (widget.id == 19209) {
					widget.defaultText.replaceAll("Total", "");
				}
				widget.secondaryText = buffer.readString();
				//System.out.println("Id: " + rsInterface.id + " - " + rsInterface.message + "\n");
			}

			if (widget.type == TYPE_MODEL_LIST || widget.type == TYPE_RECTANGLE || widget.type == TYPE_TEXT)
				widget.textColor = buffer.readInt();
			if (widget.type == TYPE_RECTANGLE || widget.type == TYPE_TEXT) {
				widget.secondaryColor = buffer.readInt();
				widget.defaultHoverColor = buffer.readInt();
				widget.secondaryHoverColor = buffer.readInt();
			}
			if (widget.type == TYPE_SPRITE) {
				widget.drawsTransparent = false;
				String name = buffer.readString();
				if (graphics != null && name.length() > 0) {
					int index = name.lastIndexOf(",");
					widget.disabledSprite = method207(Integer.parseInt(name.substring(index + 1)), graphics, name.substring(0, index));
				}
				name = buffer.readString();
				if (graphics != null && name.length() > 0) {
					int index = name.lastIndexOf(",");
					widget.enabledSprite = method207(Integer.parseInt(name.substring(index + 1)), graphics, name.substring(0, index));
				}
			}
			if (widget.type == TYPE_MODEL) {
				int content = buffer.readUnsignedByte();
				if (content != 0) {
					widget.defaultMediaType = 1;
					widget.defaultMedia = (content - 1 << 8) + buffer.readUnsignedByte();
				}
				content = buffer.readUnsignedByte();
				if (content != 0) {
					widget.anInt255 = 1;
					widget.anInt256 = (content - 1 << 8) + buffer.readUnsignedByte();
				}
				content = buffer.readUnsignedByte();
				if (content != 0)
					widget.anInt257 = (content - 1 << 8) + buffer.readUnsignedByte();
				else
					widget.anInt257 = -1;
				content = buffer.readUnsignedByte();
				if (content != 0)
					widget.anInt258 = (content - 1 << 8) + buffer.readUnsignedByte();
				else
					widget.anInt258 = -1;
				widget.modelZoom = buffer.readUShort();
				widget.modelRotation1 = buffer.readUShort();
				widget.modelRotation2 = buffer.readUShort();
			}
			if (widget.type == TYPE_ITEM_LIST) {
				widget.inventoryItemId = new int[widget.width * widget.height];
				widget.invStackSizes = new int[widget.width * widget.height];
				widget.centerText = buffer.readUnsignedByte() == 1;
				int l2 = buffer.readUnsignedByte();
				if (textDrawingAreas != null)
					widget.textDrawingAreas = textDrawingAreas[l2];
				widget.textShadow = buffer.readUnsignedByte() == 1;
				widget.textColor = buffer.readInt();
				widget.spritePaddingX = buffer.readShort();
				widget.spritePaddingY = buffer.readShort();
				widget.hasActions = buffer.readUnsignedByte() == 1;
				widget.actions = new String[5];
				for (int actionCount = 0; actionCount < 5; actionCount++) {
					widget.actions[actionCount] = buffer.readString();
					if (widget.actions[actionCount].length() == 0)
						widget.actions[actionCount] = null;
				}

			}
			if (widget.optionType == OPTION_USABLE || widget.type == TYPE_INVENTORY) {
				widget.selectedActionName = buffer.readString();
				widget.spellName = buffer.readString();
				widget.spellUsableOn = buffer.readUShort();
			}

			if (widget.type == 8)
				widget.defaultText = buffer.readString();

			if (widget.optionType == OPTION_OK || widget.optionType == OPTION_TOGGLE_SETTING || widget.optionType == OPTION_RESET_SETTING || widget.optionType == OPTION_CONTINUE) {
				widget.tooltip = buffer.readString();
				if (widget.tooltip.length() == 0) {
					//TODO
					if (widget.optionType == OPTION_OK)
						widget.tooltip = "Ok";
					if (widget.optionType == OPTION_TOGGLE_SETTING)
						widget.tooltip = "Select";
					if (widget.optionType == OPTION_RESET_SETTING)
						widget.tooltip = "Select";
					if (widget.optionType == OPTION_CONTINUE)
						widget.tooltip = "Continue";
				}
			}
		}		
		interfaceLoader = interfaces;
		clanChatTab(textDrawingAreas);
		//clanChatSetup(textDrawingAreas);
		configureLunar(textDrawingAreas);
		quickCurses(textDrawingAreas);
		quickPrayers(textDrawingAreas);
		edgevilleHomeTeleport(textDrawingAreas);
		equipmentScreen(textDrawingAreas);
		equipmentTab(textDrawingAreas);
		questTab(textDrawingAreas);
		itemsOnDeathDATA(textDrawingAreas);
		itemsKeptOnDeath(textDrawingAreas);
		itemsOnDeath(textDrawingAreas);
		//repositionModernSpells();
		
		spriteCache = null;
		
	}
	
	public static void repositionModernSpells() {

		Widget widget = Widget.interfaceCache[12424];
		for (int i = 0; i < widget.children.length; i++) {
			switch (widget.children[i]) {
			case 18470:
				widget.childX[i] = 100;
				widget.childY[i] = 100;
				break;
			}
			
		}
	}
	
	public static void itemsKeptOnDeath(GameFont[] tda) {
		Widget Interface = addInterface(22030);
		addSprite(22031, 1, "Interfaces/Death/SPRITE");
		addHoverButton(22032, "Interfaces/Death/SPRITE", 2, 17, 17, "Close",
				250, 22033, 3);
		addHoveredButton(22033, "Interfaces/Death/SPRITE", 3, 17, 17, 22034);
		addText(22035, "", tda, 0, 0xff981f, false, true);
		addText(22036, "", tda, 0, 0xff981f, false, true);
		addText(22037, "", tda, 0, 0xff981f, false, true);
		addText(22038, "", tda, 0, 0xff981f, false, true);
		addText(22039, "", tda, 0, 0xff981f, false, true);
		addText(22040, "", tda, 1, 0xffcc33, false, true);
		setChildren(9, Interface);
		setBounds(22031, 7, 8, 0, Interface);
		setBounds(22032, 480, 18, 1, Interface);
		setBounds(22033, 480, 18, 2, Interface);
		setBounds(22035, 348, 98, 3, Interface);
		setBounds(22036, 348, 110, 4, Interface);
		setBounds(22037, 348, 122, 5, Interface);
		setBounds(22038, 348, 134, 6, Interface);
		setBounds(22039, 348, 146, 7, Interface);
		setBounds(22040, 398, 297, 8, Interface);
	}
	
	public static void clanChatTab(GameFont[] tda) {
		Widget tab = addTabInterface(18128);
		addHoverButton(18129, "/Clan Chat/SPRITE", 6, 72, 32, "Join Clan", 550,
				18130, 5);
		addHoveredButton(18130, "/Clan Chat/SPRITE", 7, 72, 32, 18131);
		addHoverButton(18132, "/Clan Chat/SPRITE", 6, 72, 32, "Clan Setup", -1,
				18133, 5);
		addConfigHover(18250, 4, 18251, 1, 2, "/Clan Chat/Lootshare", 40, 40,
				308, 1, "Toggle Lootshare", 18252, 0, 1,
				"/Clan Chat/Lootshare", 18253, "Toggle xp 2", "Toggle xp 3",
				12, 20);
		addHoveredButton(18133, "/Clan Chat/SPRITE", 7, 72, 32, 18134);
		addText(18135, "Join Clan", tda, 0, 0xff9b00, true, true);
		addText(18136, "Clan Setup", tda, 0, 0xff9b00, true, true);
		addSprite(18137, 38, "/Clan Chat/SPRITE");
		addText(18138, "Clan Chat", tda, 2, 0xff9b00, true, true);
		addText(18139, "Talking in: Not in clan", tda, 0, 0xff9b00, false, true);
		addText(18140, "Owner: None", tda, 0, 0xff9b00, false, true);
		addSprite(16126, 4, "/Clan Chat/SPRITE");
		addText(18141, "(1/100)", tda, 2, 0xff9b00, true, true);
		tab.totalChildren(14);
		tab.child(0, 18137, 0, 53);
		tab.child(1, 18143, 6, 62);
		tab.child(2, 18129, 15, 226);
		tab.child(3, 18130, 15, 226);
		tab.child(4, 18132, 103, 226);
		tab.child(5, 18133, 103, 226);
		tab.child(6, 18135, 51, 237);
		tab.child(7, 18136, 139, 237);
		tab.child(8, 18138, 95, 1);
		tab.child(9, 18139, 10, 23);
		tab.child(10, 18140, 25, 38);
		tab.child(11, 18250, 153, 22);
		tab.child(12, 18252, 153, 22);
		tab.child(13, 18141, 163, 1);

		/* Text area */
		Widget list = addTabInterface(18143);
		list.totalChildren(100);
		for (int i = 18144; i <= 18244; i++) {
			addText(i, "", tda, 0, 0xffffff, false, true);
		}
		for (int id = 18144, i = 0; id <= 18243 && i <= 99; id++, i++) {
			interfaceCache[id].actions = new String[] { "Edit Rank", "Kick",
					"Ban" };
			list.children[i] = id;
			list.childX[i] = 5;
			for (int id2 = 18144, i2 = 1; id2 <= 18243 && i2 <= 99; id2++, i2++) {
				list.childY[0] = 2;
				list.childY[i2] = list.childY[i2 - 1] + 14;
			}
		}
		list.height = 158;
		list.width = 162;
		list.scrollMax = 1405;
	}
	
	

	public static void clanChatSetup(GameFont[] tda) {
		Widget tab = addTabInterface(15456);
		addSprite(15251, 0, "Clan Chat/sprite");
		addHoverButton(15252, "Clan Chat/button", 2, 150, 35, "Set prefix", -1,
				15253, 1);
		addHoveredButton(15253, "Clan Chat/button", 3, 150, 35, 15254);
		addHoverButton(15255, "Clan Chat/button", 2, 150, 35, "Anyone", -1,
				15256, 1);
		addHoveredButton(15256, "Clan Chat/button", 3, 150, 35, 15257);

		addHoverButton(16000, "b", 1, 150, 35, "Only me", -1, 15999, 1);
		addHoverButton(16001, "b", 1, 150, 35, "General+", -1, 15999, 1);
		addHoverButton(16002, "b", 1, 150, 35, "Captain+", -1, 15999, 1);
		addHoverButton(16003, "b", 1, 150, 35, "Lieutenant+", -1, 15999, 1);
		addHoverButton(16004, "b", 1, 150, 35, "Sergeant+", -1, 15999, 1);
		addHoverButton(16005, "b", 1, 150, 35, "Corporal+", -1, 15999, 1);
		addHoverButton(16006, "b", 1, 150, 35, "Recruit+", -1, 15999, 1);
		addHoverButton(16007, "b", 1, 150, 35, "Any friends", -1, 15999, 1);

		addHoverButton(15258, "Clan Chat/button", 2, 150, 35, "Anyone", -1,
				15259, 1);
		addHoveredButton(15259, "Clan Chat/button", 3, 150, 35, 15260);

		addHoverButton(16010, "b", 1, 150, 35, "Only me", -1, 15999, 1);
		addHoverButton(16011, "b", 1, 150, 35, "General+", -1, 15999, 1);
		addHoverButton(16012, "b", 1, 150, 35, "Captain+", -1, 15999, 1);
		addHoverButton(16013, "b", 1, 150, 35, "Lieutenant+", -1, 15999, 1);
		addHoverButton(16014, "b", 1, 150, 35, "Sergeant+", -1, 15999, 1);
		addHoverButton(16015, "b", 1, 150, 35, "Corporal+", -1, 15999, 1);
		addHoverButton(16016, "b", 1, 150, 35, "Recruit+", -1, 15999, 1);
		addHoverButton(16017, "b", 1, 150, 35, "Any friends", -1, 15999, 1);

		addHoverButton(15261, "Clan Chat/button", 2, 150, 35, "Only me", -1,
				15262, 1);
		addHoveredButton(15262, "Clan Chat/button", 3, 150, 35, 15263);

		// addHoverButton(48020, "b", 1, 150, 35, "Only me", -1, 47999, 1);
		addHoverButton(16221, "b", 1, 150, 35, "General+", -1, 15999, 1);
		addHoverButton(16222, "b", 1, 150, 35, "Captain+", -1, 15999, 1);
		addHoverButton(16223, "b", 1, 150, 35, "Lieutenant+", -1, 15999, 1);
		addHoverButton(16224, "b", 1, 150, 35, "Sergeant+", -1, 15999, 1);
		addHoverButton(16225, "b", 1, 150, 35, "Corporal+", -1, 15999, 1);
		addHoverButton(16226, "b", 1, 150, 35, "Recruit+", -1, 15999, 1);

		addHoverButton(15267, "Clan Chat/close", 0, 16, 16, "Close", -1, 15268,
				1);
		addHoveredButton(15268, "Clan Chat/close", 1, 16, 16, 15269);

		addText(15800, "Clan name:", tda, 0, 0xff981f, false, true);
		addText(15801, "Who can enter chat?", tda, 0, 0xff981f, false, true);
		addText(15812, "Who can talk on chat?", tda, 0, 0xff981f, false, true);
		addText(15813, "Who can kick on chat?", tda, 0, 0xff981f, false, true);
		addText(15814, "None", tda, 2, 0xffffff, true, true);
		addText(15815, "Anyone", tda, 2, 0xffffff, true, true);
		addText(15816, "Anyone", tda, 2, 0xffffff, true, true);
		addText(15817, "Only me", tda, 2, 0xffffff, true, true);

		/* Table */
		addSprite(18319, 10, "/Interfaces/Clan Chat/sprite");

		/* Label */
		addText(18320, "Name:", tda, 2, 0xFF981F, false, true);
		addText(18321, "Rank:", tda, 2, 0xFF981F, false, true);

		/* Ranked members list */

		/* Table info text */
		addText(18322, "Friends List", tda, 2, 0xFF981F, true, true);

		addText(18323, "Right click on a name to edit the member.", tda, 0,
				0xFF981F, true, true);

		/* Add ranked member button */
		addButton(18324, 0, "/Interfaces/Clan Chat/plus", "Add ranked member");
		interfaceCache[18323].optionType = 5;

		/* Add banned member button */
		addButton(18325, 0, "/Interfaces/Clan Chat/plus", "Add banned member");
		interfaceCache[18325].optionType = 5;

		/* Clan Setup title */
		addText(18303, "Clan Chat Setup", tda, 2, 0xFF981F, true, true);

		tab.totalChildren(48);
		tab.child(0, 15251, 15, 15);
		tab.child(1, 15252, 25, 50);
		tab.child(2, 15253, 25, 50);
		tab.child(3, 15267, 476, 23);
		tab.child(4, 15268, 476, 23);
		tab.child(5, 16000, 25, 87 + 10);
		tab.child(6, 16001, 25, 87 + 10);
		tab.child(7, 16002, 25, 87 + 10);
		tab.child(8, 16003, 25, 87 + 10);
		tab.child(9, 16004, 25, 87 + 10);
		tab.child(10, 16005, 25, 87 + 10);
		tab.child(11, 16006, 25, 87 + 10);
		tab.child(12, 16007, 25, 87 + 10);
		tab.child(13, 15255, 25, 87 + 10);
		tab.child(14, 15256, 25, 87 + 10);
		tab.child(15, 16010, 25, 128 + 16);
		tab.child(16, 16011, 25, 128 + 16);
		tab.child(17, 16012, 25, 128 + 16);
		tab.child(18, 16013, 25, 128 + 16);
		tab.child(19, 16014, 25, 128 + 16);
		tab.child(20, 16015, 25, 128 + 16);
		tab.child(21, 16016, 25, 128 + 16);
		tab.child(22, 16017, 25, 128 + 16);
		tab.child(23, 15258, 25, 128 + 16);
		tab.child(24, 15259, 25, 128 + 16);
		// tab.child(25, 48020, 25, 168+35);
		tab.child(25, 16221, 25, 168 + 23);
		tab.child(26, 16222, 25, 168 + 23);
		tab.child(27, 16223, 25, 168 + 23);
		tab.child(28, 16224, 25, 168 + 23);
		tab.child(29, 16225, 25, 168 + 23);
		tab.child(30, 16226, 25, 168 + 23);
		tab.child(31, 15261, 25, 168 + 23);
		tab.child(32, 15262, 25, 168 + 23);
		tab.child(33, 15800, 73, 54);
		tab.child(34, 15801, 53, 95 + 5);
		tab.child(35, 15812, 53, 136 + 10);
		tab.child(36, 15813, 53, 177 + 15);
		tab.child(37, 15814, 100, 54 + 12);
		tab.child(38, 15815, 100, 95 + 5 + 12);
		tab.child(39, 15816, 100, 136 + 10 + 12);
		tab.child(40, 15817, 100, 177 + 15 + 12);
		tab.child(41, 14100, 0 - 8, 94);
		tab.child(42, 18319, 197, 70);
		tab.child(43, 18320, 202 - 4, 74);
		tab.child(44, 18321, 339 - 4, 74);
		tab.child(45, 18322, 337, 47);
		tab.child(46, 18323, 337, 47 + 11);
		// tab.child(47, 18324, 319, 75);
		// tab.child(48, 18325, 459, 75);
		tab.child(47, 18303, 256, 26 - 5);

		tab = addTabInterface(14100);
		tab.width = 474;
		tab.height = 213;
		tab.scrollMax = 2030;
		for (int i = 14101; i <= 14300; i++)
			addText2(i, "", tda, 1, 0xffff64, false, true);

		for (int i = 17551; i <= 17750; i++) {
			addHoverText2(i, "", new String[] { "Unban", "General", "Captain",
					"Lieutenant", "Sergeant", "Corporal", "Recruit", "Guest" },
					tda, 1, 0xffffff, false, false, 150);
		}

		tab.totalChildren(400);
		int Child = 0;
		int Y = 3;
		for (int i = 14101; i <= 14300; i++) {
			tab.child(Child, i, 204, Y);
			Child++;
			Y += 13;
		}
		Y = 3;
		for (int i = 17551; i <= 17750; i++) {
			tab.child(Child, i, 343, Y);
			Child++;
			Y += 13;
		}
	}
	
	public static void addHoverText2(int id, String text, String[] tooltips,
			GameFont tda[], int idx, int color, boolean center,
			boolean textShadowed, int width) {
		Widget rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parent = id;
		rsinterface.type = 4;
		rsinterface.optionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.hoverType = -1;
		rsinterface.centerText = center;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.defaultText = text;
		rsinterface.secondaryText = "";
		rsinterface.textColor = color;
		rsinterface.secondaryColor = 0;
		rsinterface.defaultHoverColor = 0xffffff;
		rsinterface.secondaryHoverColor = 0;
		rsinterface.tooltips = tooltips;
	}
	
	public static void addText2(int id, String text, GameFont tda[],
			int idx, int color, boolean center, boolean shadow) {
		Widget tab = addTabInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 4;
		tab.optionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.defaultText = text;
		tab.secondaryText = "";
		tab.textColor = color;
		tab.secondaryColor = 0;
		tab.defaultHoverColor = 0;
		tab.secondaryHoverColor = 0;
	}
	
	public static void addSprite(int i, int j, int k) {
		Widget rsinterface = interfaceCache[i] = new Widget();
		rsinterface.id = i;
		rsinterface.parent = i;
		rsinterface.type = 5;
		rsinterface.optionType = 1;
		rsinterface.contentType = 0;
		rsinterface.width = 20;
		rsinterface.height = 20;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = 52;
		rsinterface.disabledSprite = imageLoader(j,
				"Interfaces/Equipment/SPRITE");
		rsinterface.enabledSprite = imageLoader(k,
				"Interfaces/Equipment/SPRITE");
	}
	
	public static void addText(int id, String text, GameFont wid[],
			int idx, int color) {
		Widget rsinterface = addTabInterface(id);
		rsinterface.id = id;
		rsinterface.parent = id;
		rsinterface.type = 4;
		rsinterface.optionType = 0;
		rsinterface.width = 174;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = false;
		rsinterface.textShadow = true;
		rsinterface.textDrawingAreas = wid[idx];
		rsinterface.defaultText = text;
		rsinterface.secondaryText = "";
		rsinterface.textColor = color;
		rsinterface.secondaryColor = 0;
		rsinterface.defaultHoverColor = 0;
		rsinterface.secondaryHoverColor = 0;
	}
	
	public static void itemsOnDeath(GameFont[] wid) {
		Widget rsinterface = addInterface(17100);
		addSprite(17101, 2, 2);
		// addHover(17102,"Items Kept On Death/SPRITE", 1, 17, 17, "Close", 0,
		// 10602, 1);
		// addHovered(10602,"Items Kept On Death/SPRITE", 3, 17, 17, 10603);
		addText(17103, "Items kept on death", wid, 2, 0xff981f);
		addText(17104, "Items I will keep...", wid, 1, 0xff981f);
		addText(17105, "Items I will lose...", wid, 1, 0xff981f);
		addText(17106, "Info", wid, 1, 0xff981f);
		addText(17107, "", wid, 1, 0xffcc33);
		addText(17108, "", wid, 1, 0xffcc33);
		// rsinterface.scrollMax = 50;
		rsinterface.interfaceShown = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 15210;
		rsinterface.childX[1] = 478;
		rsinterface.childY[1] = 17;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 49;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 109;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 49;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 348;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 401;
		rsinterface.childY[7] = 293;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 71;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 129;
		rsinterface.children[11] = 15211;
		rsinterface.childX[11] = 478;
		rsinterface.childY[11] = 17;
		rsinterface = interfaceCache[10494];
		rsinterface.spritePaddingX = 6;
		rsinterface.spritePaddingY = 5;
		rsinterface = interfaceCache[10600];
		rsinterface.spritePaddingX = 6;
		rsinterface.spritePaddingY = 5;
	}
	
	public static void itemsOnDeathDATA(GameFont[] tda) {
		Widget RSinterface = addInterface(17115);
		addText(17109, "", 0xff981f, false, false, 0, tda, 0);
		addText(17110, "The normal amount of", 0xff981f, false, false, 0, tda,
				0);
		addText(17111, "items kept is three.", 0xff981f, false, false, 0, tda,
				0);
		addText(17112, "", 0xff981f, false, false, 0, tda, 0);
		addText(17113, "If you are skulled,", 0xff981f, false, false, 0, tda, 0);
		addText(17114, "you will lose all your", 0xff981f, false, false, 0,
				tda, 0);
		addText(17117, "items, unless an item", 0xff981f, false, false, 0, tda,
				0);
		addText(17118, "protecting prayer is", 0xff981f, false, false, 0, tda,
				0);
		addText(17119, "used.", 0xff981f, false, false, 0, tda, 0);
		addText(17120, "", 0xff981f, false, false, 0, tda, 0);
		addText(17121, "Item protecting prayers", 0xff981f, false, false, 0,
				tda, 0);
		addText(17122, "will allow you to keep", 0xff981f, false, false, 0,
				tda, 0);
		addText(17123, "one extra item.", 0xff981f, false, false, 0, tda, 0);
		addText(17124, "", 0xff981f, false, false, 0, tda, 0);
		addText(17125, "The items kept are", 0xff981f, false, false, 0, tda, 0);
		addText(17126, "selected by the server", 0xff981f, false, false, 0,
				tda, 0);
		addText(17127, "and include the most", 0xff981f, false, false, 0, tda,
				0);
		addText(17128, "expensive items you're", 0xff981f, false, false, 0,
				tda, 0);
		addText(17129, "carrying.", 0xff981f, false, false, 0, tda, 0);
		addText(17130, "", 0xff981f, false, false, 0, tda, 0);
		RSinterface.parent = 17115;
		RSinterface.id = 17115;
		RSinterface.type = 0;
		RSinterface.optionType = 0;
		RSinterface.contentType = 0;
		RSinterface.width = 130;
		RSinterface.height = 197;
		RSinterface.opacity = 0;
		RSinterface.hoverType = -1;
		RSinterface.scrollMax = 280;
		RSinterface.children = new int[20];
		RSinterface.childX = new int[20];
		RSinterface.childY = new int[20];
		RSinterface.children[0] = 17109;
		RSinterface.childX[0] = 0;
		RSinterface.childY[0] = 0;
		RSinterface.children[1] = 17110;
		RSinterface.childX[1] = 0;
		RSinterface.childY[1] = 12;
		RSinterface.children[2] = 17111;
		RSinterface.childX[2] = 0;
		RSinterface.childY[2] = 24;
		RSinterface.children[3] = 17112;
		RSinterface.childX[3] = 0;
		RSinterface.childY[3] = 36;
		RSinterface.children[4] = 17113;
		RSinterface.childX[4] = 0;
		RSinterface.childY[4] = 48;
		RSinterface.children[5] = 17114;
		RSinterface.childX[5] = 0;
		RSinterface.childY[5] = 60;
		RSinterface.children[6] = 17117;
		RSinterface.childX[6] = 0;
		RSinterface.childY[6] = 72;
		RSinterface.children[7] = 17118;
		RSinterface.childX[7] = 0;
		RSinterface.childY[7] = 84;
		RSinterface.children[8] = 17119;
		RSinterface.childX[8] = 0;
		RSinterface.childY[8] = 96;
		RSinterface.children[9] = 17120;
		RSinterface.childX[9] = 0;
		RSinterface.childY[9] = 108;
		RSinterface.children[10] = 17121;
		RSinterface.childX[10] = 0;
		RSinterface.childY[10] = 120;
		RSinterface.children[11] = 17122;
		RSinterface.childX[11] = 0;
		RSinterface.childY[11] = 132;
		RSinterface.children[12] = 17123;
		RSinterface.childX[12] = 0;
		RSinterface.childY[12] = 144;
		RSinterface.children[13] = 17124;
		RSinterface.childX[13] = 0;
		RSinterface.childY[13] = 156;
		RSinterface.children[14] = 17125;
		RSinterface.childX[14] = 0;
		RSinterface.childY[14] = 168;
		RSinterface.children[15] = 17126;
		RSinterface.childX[15] = 0;
		RSinterface.childY[15] = 180;
		RSinterface.children[16] = 17127;
		RSinterface.childX[16] = 0;
		RSinterface.childY[16] = 192;
		RSinterface.children[17] = 17128;
		RSinterface.childX[17] = 0;
		RSinterface.childY[17] = 204;
		RSinterface.children[18] = 17129;
		RSinterface.childX[18] = 0;
		RSinterface.childY[18] = 216;
		RSinterface.children[19] = 17130;
		RSinterface.childX[19] = 0;
		RSinterface.childY[19] = 228;
	}
	
	public static void equipmentTab(GameFont[] wid) {
		Widget Interface = interfaceCache[1644];
		addSprite(15101, 0, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15102, 1, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15109, 2, "Interfaces/Equipment/bl");// cheap hax
		removeConfig(21338);
		removeConfig(21344);
		removeConfig(21342);
		removeConfig(21341);
		removeConfig(21340);
		removeConfig(15103);
		removeConfig(15104);
		// Interface.children[23] = 15101;
		// Interface.childX[23] = 40;
		// Interface.childY[23] = 205;
		Interface.children[24] = 15102;
		Interface.childX[24] = 110;
		Interface.childY[24] = 205;
		Interface.children[25] = 15109;
		Interface.childX[25] = 39;
		Interface.childY[25] = 240;
		Interface.children[26] = 27650;
		Interface.childX[26] = 0;
		Interface.childY[26] = 0;
		Interface = addInterface(27650);

		addHoverButton(27651, "Interfaces/Equipment/BOX", 2, 40, 40,
				"Price-checker", -1, 27652, 1);
		addHoveredButton(27652, "Interfaces/Equipment/HOVER", 2, 40, 40, 27658);

		addHoverButton(27653, "Interfaces/Equipment/BOX", 1, 40, 40,
				"Show Equipment Stats", -1, 27655, 1);
		addHoveredButton(27655, "Interfaces/Equipment/HOVER", 1, 40, 40, 27665);

		addHoverButton(27654, "Interfaces/Equipment/BOX", 3, 40, 40,
				"Show items kept on death", -1, 27657, 1);
		addHoveredButton(27657, "Interfaces/Equipment/HOVER", 3, 40, 40, 27666);

		setChildren(6, Interface);
		setBounds(27651, 75, 205, 0, Interface);
		setBounds(27652, 75, 205, 1, Interface);
		setBounds(27653, 23, 205, 2, Interface);
		setBounds(27654, 127, 205, 3, Interface);
		setBounds(27655, 23, 205, 4, Interface);
		setBounds(27657, 127, 205, 5, Interface);
	}
	
	public static void questTab(GameFont[] TDA) {
		Widget Interface = addInterface(638);
		setChildren(5, Interface);
		addText(29155, "Project Aeolus", 0xFF981F, false, true, 52, TDA, 2);
		addButton(29156, 1, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Achievements", 1);
		addButton(29270, 3, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Minigames", 1);
		addSprite(29157, 0, "Interfaces/QuestTab/QUEST");
		setBounds(29155, 10, 5, 0, Interface);
		setBounds(29156, 154, 4, 1, Interface);
		setBounds(29157, 3, 24, 2, Interface);
		setBounds(29160, 5, 29, 3, Interface);
		setBounds(29270, 172, 4, 4, Interface);
		Interface = addInterface(29160);
		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 1700;
		Interface.newScroller = false;
		setChildren(104, Interface);
		addText(29161, "Empty", 0xFF981F, false, true, 22, TDA, 1);
		addHoverText(29162, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29163, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29164, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29165, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		addHoverText(29166, "", "View Progress", TDA, 0, 0xff0000, false, true,
				150);
		setBounds(29161, 4, 4, 0, Interface);
		setBounds(29162, 8, 22, 1, Interface);
		setBounds(29163, 8, 35, 2, Interface);
		setBounds(29164, 8, 48, 3, Interface);
		setBounds(29165, 8, 61, 4, Interface);
		setBounds(29166, 8, 74, 5, Interface);
		int Ypos = 87;
		int frameID = 6;
		for (int iD = 29167; iD <= 29264; iD++) {
			addHoverText(iD, "", "View" + iD/* "View Quest Journal, "+iD */,
					TDA, 0, 0xff0000, false, true, 150);
			setBounds(iD, 8, Ypos, frameID, Interface);
			frameID++;
			Ypos += 13;
			Ypos++;
		}
		Interface = addInterface(29265);
		setChildren(5, Interface);
		addText(29266, "        Achievements", 0xFF981F, false, true, -1, TDA,
				2);
		addButton(29267, 2, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Quests", 1);
		addButton(29271, 3, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Minigames", 1);
		addSprite(29269, 0, "Interfaces/QuestTab/QUEST");
		setBounds(29266, 10, 5, 0, Interface);
		setBounds(29267, 154, 4, 1, Interface);
		setBounds(29269, 3, 24, 2, Interface);
		setBounds(29268, 5, 29, 3, Interface);
		setBounds(29271, 172, 4, 4, Interface);
		Interface = addInterface(29268);
		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 215;
		Interface.newScroller = false;
		setChildren(14, Interface);
		setBounds(29295, 8, 6, 0, Interface);
		setBounds(29287, 8, 21, 1, Interface);
		setBounds(29305, 8, 36, 2, Interface);
		setBounds(29306, 8, 51, 3, Interface);
		setBounds(29307, 8, 66, 4, Interface);
		setBounds(29308, 8, 81, 5, Interface);
		setBounds(29309, 8, 96, 6, Interface);
		setBounds(29310, 8, 110, 7, Interface);
		setBounds(29311, 8, 125, 8, Interface);
		setBounds(29312, 8, 155, 9, Interface);
		setBounds(29313, 8, 170, 10, Interface);
		setBounds(29314, 8, 140, 11, Interface);
		setBounds(29315, 8, 185, 12, Interface);
		setBounds(29316, 8, 200, 13, Interface);
		addHoverText(29295, "Tasks Completed: 0", "View Achievements", TDA, 0,
				65280, false, true, 150);
		addHoverText(29287, "Easy Tasks", "View Achievements", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29305, "        TODO", "View Achievements",
				TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29306, "        TODO", "View Achievements",
				TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29307, "        TODO",
				"View Achievements", TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29308, "        TODO",
				"View Achievements", TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29309, "        TODO",
				"View Achievements", TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29310, "Medium Tasks", "View Achievements", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29311, "        TODO",
				"View Achievements", TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29314, "        TODO", "View Achievements",
				TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29312, "Hard Tasks", "View Achievements", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29313, "        TODO", "View Achievements",
				TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29315, "        TODO", "View Achievements",
				TDA, 0, 0xff0000, false, true, 150);
		addHoverText(29316, "        TODO",
				"View Achievements", TDA, 0, 0xff0000, false, true, 150);

		Interface = addInterface(29300);

		addText(29301, "        MiniGames", 0xFF981F, false, true, -1, TDA, 2);
		addButton(29302, 2, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Quests", 1);
		addButton(29303, 1, "Interfaces/QuestTab/QUEST", 18, 18,
				"Swap to Achievements", 1);
		addSprite(29304, 0, "Interfaces/QuestTab/QUEST");

		setChildren(5, Interface);
		setBounds(29301, 10, 5, 0, Interface);
		setBounds(29302, 154, 4, 1, Interface);
		setBounds(29304, 3, 24, 2, Interface);
		setBounds(29350, 5, 29, 3, Interface);
		setBounds(29303, 172, 4, 4, Interface);
		Interface = addInterface(29350);

		addHoverText(29352, "Barrows", "Teleport to Barrows", TDA, 0, 0xFF981F,
				false, true, 150);
		addHoverText(29353, "Castle Wars", "Teleport to Castle Wars", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29351, "Duel Arena", "Teleport to Duel Arena", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29354, "Fight Caves", "Teleport to Fight Caves", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29355, "Fight Pits", "Teleport to Fight Pits", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29356, "Pest Control ", "Teleport to Pest Control", TDA,
				0, 0xFF981F, false, true, 150);
		addHoverText(29357, "Nightmarezone ", "Teleport to Nightmarezone", TDA,
				0, 0xFF981F, false, true, 150);
		addHoverText(29358, "Rouges Den", "Teleport to Rouges Den", TDA, 0,
				0xFF981F, false, true, 150);
		addHoverText(29359, "Warriors Guild", "Teleport to Warriors Guild",
				TDA, 0, 0xFF981F, false, true, 150);
		addHoverText(29360, "", "", TDA, 0, 0xFF981F, false, true, 150);
		addHoverText(29361, "", "", TDA, 0, 0xFF981F, false, true, 150);
		addHoverText(29362, "", "", TDA, 0, 0xFF981F, false, true, 150);

		Interface.height = 214;
		Interface.width = 165;
		Interface.scrollMax = 215;
		Interface.newScroller = false;
		setChildren(12, Interface);
		setBounds(29352, 8, 6, 0, Interface);
		setBounds(29353, 8, 21, 1, Interface);
		setBounds(29351, 8, 36, 2, Interface);
		setBounds(29354, 8, 51, 3, Interface);
		setBounds(29355, 8, 66, 4, Interface);
		setBounds(29356, 8, 81, 5, Interface);
		setBounds(29357, 8, 96, 6, Interface);
		setBounds(29358, 8, 110, 7, Interface);
		setBounds(29359, 8, 125, 8, Interface);
		setBounds(29360, 8, 140, 9, Interface);
		setBounds(29361, 8, 155, 10, Interface);
		setBounds(29362, 8, 170, 11, Interface);
	}
	
	public static void removeConfig(int id) {
		@SuppressWarnings("unused")
		Widget rsi = interfaceCache[id] = new Widget();
	}
	
	public static void addHoverText(int id, String text, String tooltip,
			GameFont tda[], int idx, int color, boolean centerText,
			boolean textShadowed, int width) {
		Widget rsinterface = addInterface(id);
		rsinterface.id = id;
		rsinterface.parent = id;
		rsinterface.type = 4;
		rsinterface.optionType = 1;
		rsinterface.width = width;
		rsinterface.height = 11;
		rsinterface.contentType = 0;
		rsinterface.opacity = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.centerText = centerText;
		rsinterface.textShadow = textShadowed;
		rsinterface.textDrawingAreas = tda[idx];
		rsinterface.defaultText = text;
		rsinterface.secondaryText = "";
		rsinterface.textColor = color;
		rsinterface.secondaryColor = 0;
		rsinterface.defaultHoverColor = 0xffffff;
		rsinterface.secondaryHoverColor = 0;
		rsinterface.tooltip = tooltip;
	}
	
	public static void equipmentScreen(GameFont[] wid) {
		Widget Interface = Widget.interfaceCache[1644];
		addButton(19144, 6, "Interfaces/Equipment/CUSTOM",
				"Show Equipment Stats");
		removeSomething(19145);
		removeSomething(19146);
		removeSomething(19147);
		// setBounds(19144, 21, 210, 23, Interface);
		setBounds(19145, 40, 210, 24, Interface);
		setBounds(19146, 40, 210, 25, Interface);
		setBounds(19147, 40, 210, 26, Interface);
		Widget tab = addTabInterface(15106);
		addSprite(15107, 7, "Interfaces/Equipment/CUSTOM");
		addHoverButton(15210, "Interfaces/Equipment/CUSTOM", 8, 21, 21,
				"Close", 250, 15211, 3);
		addHoveredButton(15211, "Interfaces/Equipment/CUSTOM", 9, 21, 21, 15212);
		addText(15111, "Equip Your Character...", wid, 2, 0xe4a146, false, true);
		addText(15112, "Attack bonus", wid, 2, 0xe4a146, false, true);
		addText(15113, "Defence bonus", wid, 2, 0xe4a146, false, true);
		addText(15114, "Other bonuses", wid, 2, 0xe4a146, false, true);
		for (int i = 1675; i <= 1684; i++) {
			textSize(i, wid, 1);
		}
		textSize(1686, wid, 1);
		textSize(1687, wid, 1);
		addChar(15125);
		tab.totalChildren(44);
		tab.child(0, 15107, 4, 20);
		tab.child(1, 15210, 476, 29);
		tab.child(2, 15211, 476, 29);
		tab.child(3, 15111, 14, 30);
		int Child = 4;
		int Y = 69;
		for (int i = 1675; i <= 1679; i++) {
			tab.child(Child, i, 20, Y);
			Child++;
			Y += 14;
		}
		tab.child(9, 1680, 20, 161);
		tab.child(10, 1681, 20, 177);
		tab.child(11, 1682, 20, 192);
		tab.child(12, 1683, 20, 207);
		tab.child(13, 1684, 20, 221);
		tab.child(14, 1686, 20, 262);
		tab.child(15, 15125, 170, 200);
		tab.child(16, 15112, 16, 55);
		tab.child(17, 1687, 20, 276);
		tab.child(18, 15113, 16, 147);
		tab.child(19, 15114, 16, 248);
		tab.child(20, 1645, 104 + 295, 149 - 52);
		tab.child(21, 1646, 399, 163);
		tab.child(22, 1647, 399, 163);
		tab.child(23, 1648, 399, 58 + 146);
		tab.child(24, 1649, 26 + 22 + 297 - 2, 110 - 44 + 118 - 13 + 5);
		tab.child(25, 1650, 321 + 22, 58 + 154);
		tab.child(26, 1651, 321 + 134, 58 + 118);
		tab.child(27, 1652, 321 + 134, 58 + 154);
		tab.child(28, 1653, 321 + 48, 58 + 81);
		tab.child(29, 1654, 321 + 107, 58 + 81);
		tab.child(30, 1655, 321 + 58, 58 + 42);
		tab.child(31, 1656, 321 + 112, 58 + 41);
		tab.child(32, 1657, 321 + 78, 58 + 4);
		tab.child(33, 1658, 321 + 37, 58 + 43);
		tab.child(34, 1659, 321 + 78, 58 + 43);
		tab.child(35, 1660, 321 + 119, 58 + 43);
		tab.child(36, 1661, 321 + 22, 58 + 82);
		tab.child(37, 1662, 321 + 78, 58 + 82);
		tab.child(38, 1663, 321 + 134, 58 + 82);
		tab.child(39, 1664, 321 + 78, 58 + 122);
		tab.child(40, 1665, 321 + 78, 58 + 162);
		tab.child(41, 1666, 321 + 22, 58 + 162);
		tab.child(42, 1667, 321 + 134, 58 + 162);
		tab.child(43, 1688, 50 + 297 - 2, 110 - 13 + 5);
		for (int i = 1675; i <= 1684; i++) {
			Widget rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			Widget rsi = interfaceCache[i];
			rsi.textColor = 0xe4a146;
			rsi.centerText = false;
		}
	}

	public static void addChar(int ID) {
		Widget t = interfaceCache[ID] = new Widget();
		t.id = ID;
		t.parent = ID;
		t.type = 6;
		t.optionType = 0;
		t.contentType = 328;
		t.width = 136;
		t.height = 168;
		t.opacity = 0;
		t.mOverInterToTrigger = 0;
		t.modelZoom = 560;
		t.modelRotation1 = 150;
		t.modelRotation2 = 0;
		t.anInt257 = -1;
		t.anInt258 = -1;
	}

	public static void edgevilleHomeTeleport(GameFont[] TDA) {
		Widget rsi = interfaceCache[21741];
		rsi.optionType = 1;
		rsi.tooltip = "Cast @gre@Edgeville Home Teleport";
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.optionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(sid, spriteName);
		tab.enabledSprite = imageLoader(sid, spriteName);
		tab.width = tab.disabledSprite.myWidth;
		tab.height = tab.enabledSprite.myHeight;
		tab.tooltip = tooltip;
	}

	public String popupString;

	public static void addTooltipBox(int id, String text) {
		Widget rsi = addInterface(id);
		rsi.id = id;
		rsi.parent = id;
		rsi.type = 8;
		rsi.popupString = text;
	}

	public static void addTooltip(int id, String text) {
		Widget rsi = addInterface(id);
		rsi.id = id;
		rsi.type = 0;
		rsi.hoverOnly = true;
		rsi.hoverType = -1;
		addTooltipBox(id + 1, text);
		rsi.totalChildren(1);
		rsi.child(0, id + 1, 0, 0);
	}

	private static Sprite CustomSpriteLoader(int id, String s) {
		long l = (TextClass.method585(s) << 8) + (long) id;
		Sprite sprite = (Sprite) spriteCache.insertFromCache(l);
		if (sprite != null) {
			return sprite;
		}
		try {
			sprite = new Sprite("/Attack/" + id + s);
			spriteCache.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public static Widget addInterface(int id) {
		Widget rsi = interfaceCache[id] = new Widget();
		rsi.id = id;
		rsi.parent = id;
		rsi.width = 512;
		rsi.height = 334;
		return rsi;
	}

	public static void addText(int id, String text, GameFont tda[], int idx, int color, boolean centered) {
		Widget rsi = interfaceCache[id] = new Widget();
		if (centered)
			rsi.centerText = true;
		rsi.textShadow = true;
		rsi.textDrawingAreas = tda[idx];
		rsi.defaultText = text;
		rsi.textColor = color;
		rsi.id = id;
		rsi.type = 4;
	}

	public static void textColor(int id, int color) {
		Widget rsi = interfaceCache[id];
		rsi.textColor = color;
	}

	public static void textSize(int id, GameFont tda[], int idx) {
		Widget rsi = interfaceCache[id];
		rsi.textDrawingAreas = tda[idx];
	}

	public static void addCacheSprite(int id, int sprite1, int sprite2, String sprites) {
		Widget rsi = interfaceCache[id] = new Widget();
		rsi.disabledSprite = method207(sprite1, interfaceLoader, sprites);
		rsi.enabledSprite = method207(sprite2, interfaceLoader, sprites);
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
	}

	public static void sprite1(int id, int sprite) {
		Widget class9 = interfaceCache[id];
		class9.disabledSprite = CustomSpriteLoader(sprite, "");
	}

	public static void addActionButton(int id, int sprite, int sprite2, int width, int height, String s) {
		Widget rsi = interfaceCache[id] = new Widget();
		rsi.disabledSprite = CustomSpriteLoader(sprite, "");
		if (sprite2 == sprite)
			rsi.enabledSprite = CustomSpriteLoader(sprite, "a");
		else
			rsi.enabledSprite = CustomSpriteLoader(sprite2, "");
		rsi.tooltip = s;
		rsi.contentType = 0;
		rsi.optionType = 1;
		rsi.width = width;
		rsi.hoverType = 52;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
	}

	public static void addToggleButton(int id, int sprite, int setconfig, int width, int height, String s) {
		Widget rsi = addInterface(id);
		rsi.disabledSprite = CustomSpriteLoader(sprite, "");
		rsi.enabledSprite = CustomSpriteLoader(sprite, "a");
		rsi.scriptDefaults = new int[1];
		rsi.scriptDefaults[0] = 1;
		rsi.scriptOperators = new int[1];
		rsi.scriptOperators[0] = 1;
		rsi.scripts = new int[1][3];
		rsi.scripts[0][0] = 5;
		rsi.scripts[0][1] = setconfig;
		rsi.scripts[0][2] = 0;
		rsi.optionType = 4;
		rsi.width = width;
		rsi.hoverType = -1;
		rsi.parent = id;
		rsi.id = id;
		rsi.type = 5;
		rsi.height = height;
		rsi.tooltip = s;
	}

	public void totalChildren(int id, int x, int y) {
		children = new int[id];
		childX = new int[x];
		childY = new int[y];
	}

	public static void removeSomething(int id) {
		@SuppressWarnings("unused")
		Widget rsi = interfaceCache[id] = new Widget();
	}

	public void specialBar(int id) // 7599
	{
		/*
		 * addActionButton(ID, SpriteOFF, SpriteON, Width, Height,
		 * "SpriteText");
		 */
		addActionButton(id - 12, 7587, -1, 150, 26, "Use @gre@Special Attack");
		/* removeSomething(ID); */
		for (int i = id - 11; i < id; i++)
			removeSomething(i);

		Widget rsi = interfaceCache[id - 12];
		rsi.width = 150;
		rsi.height = 26;

		rsi = interfaceCache[id];
		rsi.width = 150;
		rsi.height = 26;

		rsi.child(0, id - 12, 0, 0);

		rsi.child(12, id + 1, 3, 7);

		rsi.child(23, id + 12, 16, 8);

		for (int i = 13; i < 23; i++) {
			rsi.childY[i] -= 1;
		}

		rsi = interfaceCache[id + 1];
		rsi.type = 5;
		rsi.disabledSprite = CustomSpriteLoader(7600, "");

		for (int i = id + 2; i < id + 12; i++) {
			rsi = interfaceCache[i];
			rsi.type = 5;
		}

		sprite1(id + 2, 7601);
		sprite1(id + 3, 7602);
		sprite1(id + 4, 7603);
		sprite1(id + 5, 7604);
		sprite1(id + 6, 7605);
		sprite1(id + 7, 7606);
		sprite1(id + 8, 7607);
		sprite1(id + 9, 7608);
		sprite1(id + 10, 7609);
		sprite1(id + 11, 7610);
	}

	public static void Sidebar0(GameFont[] tda) {
		/*
		 * Sidebar0a(id, id2, id3, "text1", "text2", "text3", "text4", str1x,
		 * str1y, str2x, str2y, str3x, str3y, str4x, str4y, img1x, img1y, img2x,
		 * img2y, img3x, img3y, img4x, img4y, tda);
		 */
		Sidebar0a(1698, 1701, 7499, "Chop", "Hack", "Smash", "Block", 42, 75, 127, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(2276, 2279, 7574, "Stab", "Lunge", "Slash", "Block", 43, 75, 124, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(2423, 2426, 7599, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(3796, 3799, 7624, "Pound", "Pummel", "Spike", "Block", 39, 75, 121, 75, 41, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(4679, 4682, 7674, "Lunge", "Swipe", "Pound", "Block", 40, 75, 124, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(4705, 4708, 7699, "Chop", "Slash", "Smash", "Block", 42, 75, 125, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(5570, 5573, 7724, "Spike", "Impale", "Smash", "Block", 41, 75, 123, 75, 39, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0a(7762, 7765, 7800, "Chop", "Slash", "Lunge", "Block", 42, 75, 125, 75, 40, 128, 125, 128, 122, 103, 40, 50, 122, 50, 40, 103, tda);
		/*
		 * Sidebar0b(id, id2, "text1", "text2", "text3", "text4", str1x, str1y,
		 * str2x, str2y, str3x, str3y, str4x, str4y, img1x, img1y, img2x, img2y,
		 * img3x, img3y, img4x, img4y, tda);
		 */
		Sidebar0b(776, 779, "Reap", "Chop", "Jab", "Block", 42, 75, 126, 75, 46, 128, 125, 128, 122, 103, 122, 50, 40, 103, 40, 50, tda);
		/*
		 * Sidebar0c(id, id2, id3, "text1", "text2", "text3", str1x, str1y,
		 * str2x, str2y, str3x, str3y, img1x, img1y, img2x, img2y, img3x, img3y,
		 * tda);
		 */
		Sidebar0c(425, 428, 7474, "Pound", "Pummel", "Block", 39, 75, 121, 75, 42, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(1749, 1752, 7524, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(1764, 1767, 7549, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(4446, 4449, 7649, "Accurate", "Rapid", "Longrange", 33, 75, 125, 75, 29, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(5855, 5857, 7749, "Punch", "Kick", "Block", 40, 75, 129, 75, 42, 128, 40, 50, 122, 50, 40, 103, tda);
		Sidebar0c(6103, 6132, 6117, "Bash", "Pound", "Block", 43, 75, 124, 75, 42, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(8460, 8463, 8493, "Jab", "Swipe", "Fend", 46, 75, 124, 75, 43, 128, 40, 103, 40, 50, 122, 50, tda);
		Sidebar0c(12290, 12293, 12323, "Flick", "Lash", "Deflect", 44, 75, 127, 75, 36, 128, 40, 50, 40, 103, 122, 50, tda);
		/*
		 * Sidebar0d(id, id2, "text1", "text2", "text3", str1x, str1y, str2x,
		 * str2y, str3x, str3y, img1x, img1y, img2x, img2y, img3x, img3y, tda);
		 */
		Sidebar0d(328, 331, "Bash", "Pound", "Focus", 42, 66, 39, 101, 41, 136, 40, 120, 40, 50, 40, 85, tda);

		Widget rsi = addInterface(19300);
		/* textSize(ID, wid, Size); */
		textSize(3983, tda, 0);
		/* addToggleButton(id, sprite, config, width, height, wid); */
		addToggleButton(150, 150, 172, 150, 44, "Auto Retaliate");

		rsi.totalChildren(2, 2, 2);
		rsi.child(0, 3983, 52, 25); // combat level
		rsi.child(1, 150, 21, 153); // auto retaliate

		rsi = interfaceCache[3983];
		rsi.centerText = true;
		rsi.textColor = 0xff981f;
	}

	public static void Sidebar0a(int id, int id2, int id3, String text1, String text2, String text3, String text4, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, int img4x, int img4y, GameFont[] tda) // 4button
																																																																															// spec
	{
		Widget rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addText(id2 + 14, text4, tda, 0, 0xff981f, false);
		/* specialBar(ID); */
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.disabledSprite = CustomSpriteLoader(19301, "");
			rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0b(int id, int id2, String text1, String text2, String text3, String text4, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int str4x, int str4y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, int img4x, int img4y, GameFont[] tda) // 4button
																																																																													// nospec
	{
		Widget rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 11, text1, tda, 0, 0xff981f, false);
		addText(id2 + 12, text2, tda, 0, 0xff981f, false);
		addText(id2 + 13, text3, tda, 0, 0xff981f, false);
		addText(id2 + 14, text4, tda, 0, 0xff981f, false);

		rsi.width = 190;
		rsi.height = 261;

		int last = 14;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 46);
		frame++; // 2429
		rsi.child(frame, id2 + 4, 104, 99);
		frame++; // 2430
		rsi.child(frame, id2 + 5, 21, 99);
		frame++; // 2431
		rsi.child(frame, id2 + 6, 105, 46);
		frame++; // 2432

		rsi.child(frame, id2 + 7, img1x, img1y);
		frame++; // bottomright 2433
		rsi.child(frame, id2 + 8, img2x, img2y);
		frame++; // topleft 2434
		rsi.child(frame, id2 + 9, img3x, img3y);
		frame++; // bottomleft 2435
		rsi.child(frame, id2 + 10, img4x, img4y);
		frame++; // topright 2436

		rsi.child(frame, id2 + 11, str1x, str1y);
		frame++; // chop 2437
		rsi.child(frame, id2 + 12, str2x, str2y);
		frame++; // slash 2438
		rsi.child(frame, id2 + 13, str3x, str3y);
		frame++; // lunge 2439
		rsi.child(frame, id2 + 14, str4x, str4y);
		frame++; // block 2440

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon 2426

		for (int i = id2 + 3; i < id2 + 7; i++) { // 2429 - 2433
			rsi = interfaceCache[i];
			rsi.disabledSprite = CustomSpriteLoader(19301, "");
			rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0c(int id, int id2, int id3, String text1, String text2, String text3, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, GameFont[] tda) // 3button
																																																																// spec
	{
		Widget rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addText(id2 + 11, text3, tda, 0, 0xff981f, false);
		/* specialBar(ID); */
		rsi.specialBar(id3); // 7599

		rsi.width = 190;
		rsi.height = 261;

		int last = 12;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 21, 99);
		frame++;
		rsi.child(frame, id2 + 4, 105, 46);
		frame++;
		rsi.child(frame, id2 + 5, 21, 46);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // chop
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // slash
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // lunge

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
		rsi.child(frame, id3, 21, 205);
		frame++; // special attack 7599

		for (int i = id2 + 3; i < id2 + 6; i++) {
			rsi = interfaceCache[i];
			rsi.disabledSprite = CustomSpriteLoader(19301, "");
			rsi.enabledSprite = CustomSpriteLoader(19301, "a");
			rsi.width = 68;
			rsi.height = 44;
		}
	}

	public static void Sidebar0d(int id, int id2, String text1, String text2, String text3, int str1x, int str1y, int str2x, int str2y, int str3x, int str3y, int img1x, int img1y, int img2x, int img2y, int img3x, int img3y, GameFont[] tda) // 3button
																																																														// nospec
																																																														// (magic
																																																														// intf)
	{
		Widget rsi = addInterface(id); // 2423
		/* addText(ID, "Text", tda, Size, Colour, Centered); */
		addText(id2, "-2", tda, 3, 0xff981f, true); // 2426
		addText(id2 + 9, text1, tda, 0, 0xff981f, false);
		addText(id2 + 10, text2, tda, 0, 0xff981f, false);
		addText(id2 + 11, text3, tda, 0, 0xff981f, false);

		// addText(353, "Spell", tda, 0, 0xff981f, false);
		removeSomething(353);
		addText(354, "Spell", tda, 0, 0xff981f, false);

		addCacheSprite(337, 19, 0, "combaticons");
		addCacheSprite(338, 13, 0, "combaticons2");
		addCacheSprite(339, 14, 0, "combaticons2");

		/* addToggleButton(id, sprite, config, width, height, tooltip); */
		// addToggleButton(349, 349, 108, 68, 44, "Select");
		removeSomething(349);
		addToggleButton(350, 350, 108, 68, 44, "Select");

		rsi.width = 190;
		rsi.height = 261;

		int last = 15;
		int frame = 0;
		rsi.totalChildren(last, last, last);

		rsi.child(frame, id2 + 3, 20, 115);
		frame++;
		rsi.child(frame, id2 + 4, 20, 80);
		frame++;
		rsi.child(frame, id2 + 5, 20, 45);
		frame++;

		rsi.child(frame, id2 + 6, img1x, img1y);
		frame++; // topleft
		rsi.child(frame, id2 + 7, img2x, img2y);
		frame++; // bottomleft
		rsi.child(frame, id2 + 8, img3x, img3y);
		frame++; // topright

		rsi.child(frame, id2 + 9, str1x, str1y);
		frame++; // bash
		rsi.child(frame, id2 + 10, str2x, str2y);
		frame++; // pound
		rsi.child(frame, id2 + 11, str3x, str3y);
		frame++; // focus

		rsi.child(frame, 349, 105, 46);
		frame++; // spell1
		rsi.child(frame, 350, 104, 106);
		frame++; // spell2

		rsi.child(frame, 353, 125, 74);
		frame++; // spell
		rsi.child(frame, 354, 125, 134);
		frame++; // spell

		rsi.child(frame, 19300, 0, 0);
		frame++; // stuffs
		rsi.child(frame, id2, 94, 4);
		frame++; // weapon
	}

	public static void emoteTab() {
		Widget tab = addTabInterface(147);
		Widget scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		addButton(168, 0, "/Emotes/EMOTE", "Yes", 41, 47);
		addButton(169, 1, "/Emotes/EMOTE", "No", 41, 47);
		addButton(164, 2, "/Emotes/EMOTE", "Bow", 41, 47);
		addButton(165, 3, "/Emotes/EMOTE", "Angry", 41, 47);
		addButton(162, 4, "/Emotes/EMOTE", "Think", 41, 47);
		addButton(163, 5, "/Emotes/EMOTE", "Wave", 41, 47);
		addButton(13370, 6, "/Emotes/EMOTE", "Shrug", 41, 47);
		addButton(171, 7, "/Emotes/EMOTE", "Cheer", 41, 47);
		addButton(167, 8, "/Emotes/EMOTE", "Beckon", 41, 47);
		addButton(170, 9, "/Emotes/EMOTE", "Laugh", 41, 47);
		addButton(13366, 10, "/Emotes/EMOTE", "Jump for Joy", 41, 47);
		addButton(13368, 11, "/Emotes/EMOTE", "Yawn", 41, 47);
		addButton(166, 12, "/Emotes/EMOTE", "Dance", 41, 47);
		addButton(13363, 13, "/Emotes/EMOTE", "Jig", 41, 47);
		addButton(13364, 14, "/Emotes/EMOTE", "Spin", 41, 47);
		addButton(13365, 15, "/Emotes/EMOTE", "Headbang", 41, 47);
		addButton(161, 16, "/Emotes/EMOTE", "Cry", 41, 47);
		addButton(11100, 17, "/Emotes/EMOTE", "Blow kiss", 41, 47);
		addButton(13362, 18, "/Emotes/EMOTE", "Panic", 41, 47);
		addButton(13367, 19, "/Emotes/EMOTE", "Raspberry", 41, 47);
		addButton(172, 20, "/Emotes/EMOTE", "Clap", 41, 47);
		addButton(13369, 21, "/Emotes/EMOTE", "Salute", 41, 47);
		addButton(13383, 22, "/Emotes/EMOTE", "Goblin Bow", 41, 47);
		addButton(13384, 23, "/Emotes/EMOTE", "Goblin Salute", 41, 47);
		addButton(667, 24, "/Emotes/EMOTE", "Glass Box", 41, 47);
		addButton(6503, 25, "/Emotes/EMOTE", "Climb Rope", 41, 47);
		addButton(6506, 26, "/Emotes/EMOTE", "Lean On Air", 41, 47);
		addButton(666, 27, "/Emotes/EMOTE", "Glass Wall", 41, 47);
		addButton(18464, 28, "/Emotes/EMOTE", "Zombie Walk", 41, 47);
		addButton(18465, 29, "/Emotes/EMOTE", "Zombie Dance", 41, 47);
		addButton(15166, 30, "/Emotes/EMOTE", "Scared", 41, 47);
		addButton(18686, 31, "/Emotes/EMOTE", "Rabbit Hop", 41, 47);
		addConfigButton(154, 147, 32, 33, "EMOTE", 41, 47, "Skillcape Emote", 0, 1, 700);
		scroll.totalChildren(33);
		scroll.child(0, 168, 10, 7);
		scroll.child(1, 169, 54, 7);
		scroll.child(2, 164, 98, 14);
		scroll.child(3, 165, 137, 7);
		scroll.child(4, 162, 9, 56);
		scroll.child(5, 163, 48, 56);
		scroll.child(6, 13370, 95, 56);
		scroll.child(7, 171, 137, 56);
		scroll.child(8, 167, 7, 105);
		scroll.child(9, 170, 51, 105);
		scroll.child(10, 13366, 95, 104);
		scroll.child(11, 13368, 139, 105);
		scroll.child(12, 166, 6, 154);
		scroll.child(13, 13363, 50, 154);
		scroll.child(14, 13364, 90, 154);
		scroll.child(15, 13365, 135, 154);
		scroll.child(16, 161, 8, 204);
		scroll.child(17, 11100, 51, 203);
		scroll.child(18, 13362, 99, 204);
		scroll.child(19, 13367, 137, 203);
		scroll.child(20, 172, 10, 253);
		scroll.child(21, 13369, 53, 253);
		scroll.child(22, 13383, 88, 258);
		scroll.child(23, 13384, 138, 252);
		scroll.child(24, 667, 2, 303);
		scroll.child(25, 6503, 49, 302);
		scroll.child(26, 6506, 93, 302);
		scroll.child(27, 666, 137, 302);
		scroll.child(28, 18464, 9, 352);
		scroll.child(29, 18465, 50, 352);
		scroll.child(30, 15166, 94, 356);
		scroll.child(31, 18686, 141, 353);
		scroll.child(32, 154, 5, 401);
		scroll.width = 173;
		scroll.height = 258;
		scroll.scrollMax = 403;
	}

	public static void quickCurses(GameFont[] TDA) {
		Widget tab = addTabInterface(17234);
		addTransparentSprite(17229, 0, "Quicks/Quickprayers", 50);
		addSprite(17201, 3, "Quicks/Quickprayers");
		addText(17230, "Select your quick prayers:", TDA, 0, 0xFF981F, false, true);
		for (int i = 17202, j = 630; i <= 17228 || j <= 656; i++, j++) {
			addConfigButton(i, 17200, 2, 1, "Quicks/Quickprayers", 14, 15, "Select", 0, 1, j);
		}
		addHoverButton(17231, "Quicks/Quickprayers", 4, 190, 24, "Confirm Selection", -1, 17232, 1);
		addHoveredButton(17232, "Quicks/Quickprayers", 5, 190, 24, 17233);
		int frame = 0;
		setChildren(46, tab);
		setBounds(21358, 11, 8 + 20, frame++, tab);
		setBounds(21360, 50, 11 + 20, frame++, tab);
		setBounds(21362, 87, 11 + 20, frame++, tab);
		setBounds(21364, 122, 10 + 20, frame++, tab);
		setBounds(21366, 159, 11 + 20, frame++, tab);
		setBounds(21368, 12, 45 + 20, frame++, tab);
		setBounds(21370, 46, 45 + 20, frame++, tab);
		setBounds(21372, 83, 46 + 20, frame++, tab);
		setBounds(21374, 119, 45 + 20, frame++, tab);
		setBounds(21376, 157, 45 + 20, frame++, tab);
		setBounds(21378, 11, 83 + 20, frame++, tab);
		setBounds(21380, 49, 84 + 20, frame++, tab);
		setBounds(21382, 84, 83 + 20, frame++, tab);
		setBounds(21384, 123, 84 + 20, frame++, tab);
		setBounds(21386, 159, 83 + 20, frame++, tab);
		setBounds(21388, 12, 119 + 20, frame++, tab);
		setBounds(21390, 49, 119 + 20, frame++, tab);
		setBounds(21392, 88, 119 + 20, frame++, tab);
		setBounds(21394, 122, 121 + 20, frame++, tab);
		setBounds(21396, 155, 122 + 20, frame++, tab);
		setBounds(17229, 0, 25, frame++, tab);// Faded backing
		setBounds(17201, 0, 22, frame++, tab);// Split
		setBounds(17201, 0, 237, frame++, tab);// Split
		setBounds(17202, 13 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 52 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 90 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 126 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 162 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 13 - 3, 45 + 17, frame++, tab);
		setBounds(17208, 52 - 3, 45 + 17, frame++, tab);
		setBounds(17209, 90 - 3, 45 + 17, frame++, tab);
		setBounds(17210, 126 - 3, 45 + 17, frame++, tab);
		setBounds(17211, 162 - 3, 45 + 17, frame++, tab);
		setBounds(17212, 13 - 3, 80 + 17, frame++, tab);
		setBounds(17213, 52 - 3, 80 + 17, frame++, tab);
		setBounds(17214, 90 - 3, 80 + 17, frame++, tab);
		setBounds(17215, 126 - 3, 80 + 17, frame++, tab);
		setBounds(17216, 162 - 3, 80 + 17, frame++, tab);
		setBounds(17217, 13 - 3, 119 + 17, frame++, tab);
		setBounds(17218, 52 - 3, 119 + 17, frame++, tab);
		setBounds(17219, 90 - 3, 119 + 17, frame++, tab);
		setBounds(17220, 126 - 3, 119 + 17, frame++, tab);
		setBounds(17221, 162 - 3, 119 + 17, frame++, tab);
		setBounds(17230, 5, 5, frame++, tab);// text
		setBounds(17231, 0, 237, frame++, tab);// confirm
		setBounds(17232, 0, 237, frame++, tab);// Confirm hover
	}

	public static void quickPrayers(GameFont[] TDA) {
		int frame = 0;
		Widget tab = addTabInterface(17200);

		setChildren(58, tab);//
		setBounds(5632, 5, 8 + 20, frame++, tab);
		setBounds(5633, 44, 8 + 20, frame++, tab);
		setBounds(5634, 79, 11 + 20, frame++, tab);
		setBounds(19813, 116, 10 + 20, frame++, tab);
		setBounds(19815, 153, 9 + 20, frame++, tab);
		setBounds(5635, 5, 48 + 20, frame++, tab);
		setBounds(5636, 44, 47 + 20, frame++, tab);
		setBounds(5637, 79, 49 + 20, frame++, tab);
		setBounds(5638, 116, 50 + 20, frame++, tab);
		setBounds(5639, 154, 50 + 20, frame++, tab);
		setBounds(5640, 4, 84 + 20, frame++, tab);
		setBounds(19817, 44, 87 + 20, frame++, tab);
		setBounds(19820, 81, 85 + 20, frame++, tab);
		setBounds(5641, 117, 85 + 20, frame++, tab);
		setBounds(5642, 156, 87 + 20, frame++, tab);
		setBounds(5643, 5, 125 + 20, frame++, tab);
		setBounds(5644, 43, 124 + 20, frame++, tab);
		setBounds(13984, 83, 124 + 20, frame++, tab);
		setBounds(5645, 115, 121 + 20, frame++, tab);
		setBounds(19822, 154, 124 + 20, frame++, tab);
		setBounds(19824, 5, 160 + 20, frame++, tab);
		setBounds(5649, 41, 158 + 20, frame++, tab);
		setBounds(5647, 79, 163 + 20, frame++, tab);
		setBounds(5648, 116, 158 + 20, frame++, tab);
		setBounds(19826, 161, 160 + 20, frame++, tab);
		setBounds(19828, 4, 207 + 12, frame++, tab);

		setBounds(17229, 0, 25, frame++, tab);// Faded backing
		setBounds(17201, 0, 22, frame++, tab);// Split
		setBounds(17201, 0, 237, frame++, tab);// Split

		setBounds(17202, 5 - 3, 8 + 17, frame++, tab);
		setBounds(17203, 44 - 3, 8 + 17, frame++, tab);
		setBounds(17204, 79 - 3, 8 + 17, frame++, tab);
		setBounds(17205, 116 - 3, 8 + 17, frame++, tab);
		setBounds(17206, 153 - 3, 8 + 17, frame++, tab);
		setBounds(17207, 5 - 3, 48 + 17, frame++, tab);
		setBounds(17208, 44 - 3, 48 + 17, frame++, tab);
		setBounds(17209, 79 - 3, 48 + 17, frame++, tab);
		setBounds(17210, 116 - 3, 48 + 17, frame++, tab);
		setBounds(17211, 153 - 3, 48 + 17, frame++, tab);
		setBounds(17212, 5 - 3, 85 + 17, frame++, tab);
		setBounds(17213, 44 - 3, 85 + 17, frame++, tab);
		setBounds(17214, 79 - 3, 85 + 17, frame++, tab);
		setBounds(17215, 116 - 3, 85 + 17, frame++, tab);
		setBounds(17216, 153 - 3, 85 + 17, frame++, tab);
		setBounds(17217, 5 - 3, 124 + 17, frame++, tab);
		setBounds(17218, 44 - 3, 124 + 17, frame++, tab);
		setBounds(17219, 79 - 3, 124 + 17, frame++, tab);
		setBounds(17220, 116 - 3, 124 + 17, frame++, tab);
		setBounds(17221, 153 - 3, 124 + 17, frame++, tab);
		setBounds(17222, 5 - 3, 160 + 17, frame++, tab);
		setBounds(17223, 44 - 3, 160 + 17, frame++, tab);
		setBounds(17224, 79 - 3, 160 + 17, frame++, tab);
		setBounds(17225, 116 - 3, 160 + 17, frame++, tab);
		setBounds(17226, 153 - 3, 160 + 17, frame++, tab);
		setBounds(17227, 4 - 3, 207 + 4, frame++, tab);

		setBounds(17230, 5, 5, frame++, tab);// text
		setBounds(17231, 0, 237, frame++, tab);// confirm
		setBounds(17232, 0, 237, frame++, tab);// Confirm hover
	}

	public int transparency;

	private static void addTransparentSprite(int id, int spriteId, String spriteName, int transparency) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.optionType = 0;
		tab.contentType = 0;
		tab.transparency = (byte) transparency;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(spriteId, spriteName);
		tab.enabledSprite = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static void Pestpanel(GameFont[] tda) {
		Widget RSinterface = addInterface(21119);
		addText(21120, "What", 0x999999, false, true, 52, tda, 1);
		addText(21121, "What", 0x33cc00, false, true, 52, tda, 1);
		addText(21122, "(Need 5 to 25 players)", 0xFFcc33, false, true, 52, tda, 1);
		addText(21123, "Points", 0x33ccff, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21120, 15, 12, 0, RSinterface);
		setBounds(21121, 15, 30, 1, RSinterface);
		setBounds(21122, 15, 48, 2, RSinterface);
		setBounds(21123, 15, 66, 3, RSinterface);
	}

	public static void Pestpanel2(GameFont[] tda) {
		Widget RSinterface = addInterface(21100);
		addSprite(21101, 0, "Pest Control/PEST1");
		addSprite(21102, 1, "Pest Control/PEST1");
		addSprite(21103, 2, "Pest Control/PEST1");
		addSprite(21104, 3, "Pest Control/PEST1");
		addSprite(21105, 4, "Pest Control/PEST1");
		addSprite(21106, 5, "Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, tda, 1);
		addText(21108, "", 0x0000FF, false, true, 52, tda, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, tda, 1);
		addText(21110, "", 0xCC0000, false, true, 52, tda, 1);
		addText(21111, "250", 0x99FF33, false, true, 52, tda, 1);// w purp
		addText(21112, "250", 0x99FF33, false, true, 52, tda, 1);// e blue
		addText(21113, "250", 0x99FF33, false, true, 52, tda, 1);// se yel
		addText(21114, "250", 0x99FF33, false, true, 52, tda, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, tda, 1);// attacks
		addText(21116, "0", 0x99FF33, false, true, 52, tda, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, tda, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 26, 0, RSinterface);
		setBounds(21102, 396, 26, 1, RSinterface);
		setBounds(21103, 436, 26, 2, RSinterface);
		setBounds(21104, 474, 26, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 10, 10, RSinterface);
		setBounds(21112, 398, 10, 11, RSinterface);
		setBounds(21113, 436, 10, 12, RSinterface);
		setBounds(21114, 475, 10, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public String hoverText;

	public static void addHoverBox(int id, int ParentID, String text, String text2, int configId, int configFrame) {
		Widget rsi = addTabInterface(id);
		rsi.id = id;
		rsi.parent = ParentID;
		rsi.type = 8;
		rsi.secondaryText = text;
		rsi.defaultText = text2;
		rsi.scriptOperators = new int[1];
		rsi.scriptDefaults = new int[1];
		rsi.scriptOperators[0] = 1;
		rsi.scriptDefaults[0] = configId;
		rsi.scripts = new int[1][3];
		rsi.scripts[0][0] = 5;
		rsi.scripts[0][1] = configFrame;
		rsi.scripts[0][2] = 0;
	}

	public static void addText(int id, String text, GameFont tda[], int idx, int color, boolean center, boolean shadow) {
		Widget tab = addTabInterface(id);
		tab.parent = id;
		tab.id = id;
		tab.type = 4;
		tab.optionType = 0;
		tab.width = 0;
		tab.height = 11;
		tab.contentType = 0;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.centerText = center;
		tab.textShadow = shadow;
		tab.textDrawingAreas = tda[idx];
		tab.defaultText = text;
		tab.secondaryText = "";
		tab.textColor = color;
		tab.secondaryColor = 0;
		tab.defaultHoverColor = 0;
		tab.secondaryHoverColor = 0;
	}

	public static void addText(int i, String s, int k, boolean l, boolean m, int a, GameFont[] TDA, int j) {
		Widget RSInterface = addInterface(i);
		RSInterface.parent = i;
		RSInterface.id = i;
		RSInterface.type = 4;
		RSInterface.optionType = 0;
		RSInterface.width = 0;
		RSInterface.height = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = a;
		RSInterface.centerText = l;
		RSInterface.textShadow = m;
		RSInterface.textDrawingAreas = TDA[j];
		RSInterface.defaultText = s;
		RSInterface.secondaryText = "";
		RSInterface.textColor = k;
	}

	public static void addButton(int id, int sid, String spriteName, String tooltip, int w, int h) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.optionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(sid, spriteName);
		tab.enabledSprite = imageLoader(sid, spriteName);
		tab.width = w;
		tab.height = h;
		tab.tooltip = tooltip;
	}

	public static void addConfigButton(int ID, int pID, int bID, int bID2, String bName, int width, int height, String tT, int configID, int aT, int configFrame) {
		Widget Tab = addTabInterface(ID);
		Tab.parent = pID;
		Tab.id = ID;
		Tab.type = 5;
		Tab.optionType = aT;
		Tab.contentType = 0;
		Tab.width = width;
		Tab.height = height;
		Tab.opacity = 0;
		Tab.hoverType = -1;
		Tab.scriptOperators = new int[1];
		Tab.scriptDefaults = new int[1];
		Tab.scriptOperators[0] = 1;
		Tab.scriptDefaults[0] = configID;
		Tab.scripts = new int[1][3];
		Tab.scripts[0][0] = 5;
		Tab.scripts[0][1] = configFrame;
		Tab.scripts[0][2] = 0;
		Tab.disabledSprite = imageLoader(bID, bName);
		Tab.enabledSprite = imageLoader(bID2, bName);
		Tab.tooltip = tT;
	}

	public static void addSprite(int id, int spriteId, String spriteName) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.optionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(spriteId, spriteName);
		tab.enabledSprite = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
	}

	public static void addHoverButton(int i, String imageName, int j, int width, int height, String text, int contentType, int hoverOver, int aT) {// hoverable
																																					// button
		Widget tab = addTabInterface(i);
		tab.id = i;
		tab.parent = i;
		tab.type = 5;
		tab.optionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.hoverType = hoverOver;
		tab.disabledSprite = imageLoader(j, imageName);
		tab.enabledSprite = imageLoader(j, imageName);
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton(int i, String imageName, int j, int w, int h, int IMAGEID) {// hoverable
																									// button
		Widget tab = addTabInterface(i);
		tab.parent = i;
		tab.id = i;
		tab.type = 0;
		tab.optionType = 0;
		tab.width = w;
		tab.height = h;
		tab.hoverOnly = true;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.scrollMax = 0;
		addHoverImage(IMAGEID, j, j, imageName);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverImage(int i, int j, int k, String name) {
		Widget tab = addTabInterface(i);
		tab.id = i;
		tab.parent = i;
		tab.type = 5;
		tab.optionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(j, name);
		tab.enabledSprite = imageLoader(k, name);
	}

	public static void addTransparentSprite(int id, int spriteId, String spriteName) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 5;
		tab.optionType = 0;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.disabledSprite = imageLoader(spriteId, spriteName);
		tab.enabledSprite = imageLoader(spriteId, spriteName);
		tab.width = 512;
		tab.height = 334;
		tab.drawsTransparent = true;
	}

	public static Widget addScreenInterface(int id) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;
		tab.parent = id;
		tab.type = 0;
		tab.optionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = (byte) 0;
		tab.hoverType = 0;
		return tab;
	}

	public static Widget addTabInterface(int id) {
		Widget tab = interfaceCache[id] = new Widget();
		tab.id = id;// 250
		tab.parent = id;// 236
		tab.type = 0;// 262
		tab.optionType = 0;// 217
		tab.contentType = 0;
		tab.width = 512;// 220
		tab.height = 700;// 267
		tab.opacity = (byte) 0;
		tab.hoverType = -1;// Int 230
		return tab;
	}

	private static Sprite imageLoader(int i, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteCache.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(s + " " + i);
			spriteCache.removeFromCache(sprite, l);
		} catch (Exception exception) {
			return null;
		}
		return sprite;
	}

	public void child(int id, int interID, int x, int y) {
		children[id] = interID;
		childX[id] = x;
		childY[id] = y;
	}

	public void totalChildren(int t) {
		children = new int[t];
		childX = new int[t];
		childY = new int[t];
	}

	private Model method206(int i, int j) {
		Model model = (Model) models.insertFromCache((i << 16) + j);
		if (model != null)
			return model;
		if (i == 1)
			model = Model.getModel(j);
		if (i == 2)
			model = NpcDefinition.lookup(j).model();
		if (i == 3)
			model = Game.localPlayer.method453();
		if (i == 4)
			model = ItemDefinition.lookup(j).getUnshadedModel(50);
		if (i == 5)
			model = null;
		if (model != null)
			models.removeFromCache(model, (i << 16) + j);
		return model;
	}

	private static Sprite method207(int i, CacheArchive streamLoader, String s) {
		long l = (TextClass.method585(s) << 8) + (long) i;
		Sprite sprite = (Sprite) spriteCache.insertFromCache(l);
		if (sprite != null)
			return sprite;
		try {
			sprite = new Sprite(streamLoader, s, i);
			spriteCache.removeFromCache(sprite, l);
		} catch (Exception _ex) {
			return null;
		}
		return sprite;
	}

	public static void method208(boolean flag, Model model) {
		int i = 0;// was parameter
		int j = 5;// was parameter
		if (flag)
			return;
		models.unlinkAll();
		if (model != null && j != 4)
			models.removeFromCache(model, (j << 16) + i);
	}

	public Model method209(int j, int k, boolean flag) {
		Model model;
		if (flag)
			model = method206(anInt255, anInt256);
		else
			model = method206(defaultMediaType, defaultMedia);
		if (model == null)
			return null;
		if (k == -1 && j == -1 && model.anIntArray1640 == null)
			return model;
		Model model_1 = new Model(true, SequenceFrame.method532(k) & SequenceFrame.method532(j), false, model);
		if (k != -1 || j != -1)
			model_1.skin();
		if (k != -1)
			model_1.apply(k);
		if (j != -1)
			model_1.apply(j);
		model_1.light(64, 768, -50, -10, -50, true);
		return model_1;
	}

	public Widget() {
	}

	public static CacheArchive interfaceLoader;
	public boolean drawsTransparent;
	public Sprite disabledSprite;
	public int anInt208;
	public Sprite sprites[];
	public static Widget interfaceCache[];
	public int scriptDefaults[];
	public int contentType;
	public int spritesX[];
	public int defaultHoverColor;
	public int optionType;
	public String spellName;
	public int secondaryColor;
	public int width;
	public String tooltip;
	public String selectedActionName;
	public boolean centerText;
	public int scrollPosition;
	public String actions[];
	public int scripts[][];
	public boolean filled;
	public String secondaryText;
	public int hoverType;
	public int spritePaddingX;
	public int textColor;
	public int defaultMediaType;
	public int defaultMedia;
	public boolean replaceItems;
	public int parent;
	public int spellUsableOn;
	private static ReferenceCache spriteCache;
	public int secondaryHoverColor;
	public int children[];
	public int childX[];
	public boolean usableItems;
	public GameFont textDrawingAreas;
	public int spritePaddingY;
	public int scriptOperators[];
	public int anInt246;
	public int spritesY[];
	public String defaultText;
	public boolean hasActions;
	public int id;
	public int invStackSizes[];
	public int inventoryItemId[];
	public byte opacity;
	private int anInt255;
	private int anInt256;
	public int anInt257;
	public int anInt258;
	public boolean aBoolean259;
	public Sprite enabledSprite;
	public int scrollMax;
	public int type;
	public int x;
	private static final ReferenceCache models = new ReferenceCache(30);
	public int anInt265;
	public boolean hoverOnly;
	public int height;
	public boolean textShadow;
	public int modelZoom;
	public int modelRotation1;
	public int modelRotation2;
	public int childY[];

	public static void addLunarSprite(int i, int j, String name) {
		Widget RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parent = i;
		RSInterface.type = 5;
		RSInterface.optionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.disabledSprite = imageLoader(j, name);
		RSInterface.width = 500;
		RSInterface.height = 500;
		RSInterface.tooltip = "";
	}

	public static void drawRune(int i, int id, String runeName) {
		Widget RSInterface = addInterface(i);
		RSInterface.type = 5;
		RSInterface.optionType = 0;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.disabledSprite = imageLoader(id, "Lunar/RUNE");
		RSInterface.width = 500;
		RSInterface.height = 500;
	}

	public static void addRuneText(int ID, int runeAmount, int RuneID, GameFont[] font) {
		Widget rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parent = 1151;
		rsInterface.type = 4;
		rsInterface.optionType = 0;
		rsInterface.contentType = 0;
		rsInterface.width = 0;
		rsInterface.height = 14;
		rsInterface.opacity = 0;
		rsInterface.hoverType = -1;
		rsInterface.scriptOperators = new int[1];
		rsInterface.scriptDefaults = new int[1];
		rsInterface.scriptOperators[0] = 3;
		rsInterface.scriptDefaults[0] = runeAmount;
		rsInterface.scripts = new int[1][4];
		rsInterface.scripts[0][0] = 4;
		rsInterface.scripts[0][1] = 3214;
		rsInterface.scripts[0][2] = RuneID;
		rsInterface.scripts[0][3] = 0;
		rsInterface.centerText = true;
		rsInterface.textDrawingAreas = font[0];
		rsInterface.textShadow = true;
		rsInterface.defaultText = "%1/" + runeAmount + "";
		rsInterface.secondaryText = "";
		rsInterface.textColor = 12582912;
		rsInterface.secondaryColor = 49152;
	}

	public static void homeTeleport() {
		Widget RSInterface = addInterface(30000);
		RSInterface.tooltip = "Cast @gre@Lunar Home Teleport";
		RSInterface.id = 30000;
		RSInterface.parent = 30000;
		RSInterface.type = 5;
		RSInterface.optionType = 5;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 30001;
		RSInterface.disabledSprite = imageLoader(1, "Lunar/SPRITE");
		RSInterface.width = 20;
		RSInterface.height = 20;
		Widget Int = addInterface(30001);
		Int.hoverOnly = true;
		Int.hoverType = -1;
		setChildren(1, Int);
		addLunarSprite(30002, 0, "SPRITE");
		setBounds(30002, 0, 0, 0, Int);
	}

	public static void addLunar2RunesSmallBox(int ID, int r1, int r2, int ra1, int ra2, int rune1, int lvl, String name, String descr, GameFont[] TDA, int sid, int suo, int type) {
		Widget rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parent = 1151;
		rsInterface.type = 5;
		rsInterface.optionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast On";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.scriptOperators = new int[3];
		rsInterface.scriptDefaults = new int[3];
		rsInterface.scriptOperators[0] = 3;
		rsInterface.scriptDefaults[0] = ra1;
		rsInterface.scriptOperators[1] = 3;
		rsInterface.scriptDefaults[1] = ra2;
		rsInterface.scriptOperators[2] = 3;
		rsInterface.scriptDefaults[2] = lvl;
		rsInterface.scripts = new int[3][];
		rsInterface.scripts[0] = new int[4];
		rsInterface.scripts[0][0] = 4;
		rsInterface.scripts[0][1] = 3214;
		rsInterface.scripts[0][2] = r1;
		rsInterface.scripts[0][3] = 0;
		rsInterface.scripts[1] = new int[4];
		rsInterface.scripts[1][0] = 4;
		rsInterface.scripts[1][1] = 3214;
		rsInterface.scripts[1][2] = r2;
		rsInterface.scripts[1][3] = 0;
		rsInterface.scripts[2] = new int[3];
		rsInterface.scripts[2][0] = 1;
		rsInterface.scripts[2][1] = 6;
		rsInterface.scripts[2][2] = 0;
		rsInterface.enabledSprite = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.disabledSprite = imageLoader(sid, "Lunar/LUNAROFF");
		Widget INT = addInterface(ID + 1);
		INT.hoverOnly = true;
		INT.hoverType = -1;
		setChildren(7, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 37, 35, 3, INT);// Rune
		setBounds(rune1, 112, 35, 4, INT);// Rune
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 50, 66, 5, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 123, 66, 6, INT);
	}

	public static void addLunar3RunesSmallBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, GameFont[] TDA, int sid, int suo, int type) {
		Widget rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parent = 1151;
		rsInterface.type = 5;
		rsInterface.optionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.scriptOperators = new int[4];
		rsInterface.scriptDefaults = new int[4];
		rsInterface.scriptOperators[0] = 3;
		rsInterface.scriptDefaults[0] = ra1;
		rsInterface.scriptOperators[1] = 3;
		rsInterface.scriptDefaults[1] = ra2;
		rsInterface.scriptOperators[2] = 3;
		rsInterface.scriptDefaults[2] = ra3;
		rsInterface.scriptOperators[3] = 3;
		rsInterface.scriptDefaults[3] = lvl;
		rsInterface.scripts = new int[4][];
		rsInterface.scripts[0] = new int[4];
		rsInterface.scripts[0][0] = 4;
		rsInterface.scripts[0][1] = 3214;
		rsInterface.scripts[0][2] = r1;
		rsInterface.scripts[0][3] = 0;
		rsInterface.scripts[1] = new int[4];
		rsInterface.scripts[1][0] = 4;
		rsInterface.scripts[1][1] = 3214;
		rsInterface.scripts[1][2] = r2;
		rsInterface.scripts[1][3] = 0;
		rsInterface.scripts[2] = new int[4];
		rsInterface.scripts[2][0] = 4;
		rsInterface.scripts[2][1] = 3214;
		rsInterface.scripts[2][2] = r3;
		rsInterface.scripts[2][3] = 0;
		rsInterface.scripts[3] = new int[3];
		rsInterface.scripts[3][0] = 1;
		rsInterface.scripts[3][1] = 6;
		rsInterface.scripts[3][2] = 0;
		rsInterface.enabledSprite = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.disabledSprite = imageLoader(sid, "Lunar/LUNAROFF");
		Widget INT = addInterface(ID + 1);
		INT.hoverOnly = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 0, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 19, 2, INT);
		setBounds(30016, 14, 35, 3, INT);
		setBounds(rune1, 74, 35, 4, INT);
		setBounds(rune2, 130, 35, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 66, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 66, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 66, 8, INT);
	}

	public static void addLunar3RunesBigBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, GameFont[] TDA, int sid, int suo, int type) {
		Widget rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parent = 1151;
		rsInterface.type = 5;
		rsInterface.optionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.scriptOperators = new int[4];
		rsInterface.scriptDefaults = new int[4];
		rsInterface.scriptOperators[0] = 3;
		rsInterface.scriptDefaults[0] = ra1;
		rsInterface.scriptOperators[1] = 3;
		rsInterface.scriptDefaults[1] = ra2;
		rsInterface.scriptOperators[2] = 3;
		rsInterface.scriptDefaults[2] = ra3;
		rsInterface.scriptOperators[3] = 3;
		rsInterface.scriptDefaults[3] = lvl;
		rsInterface.scripts = new int[4][];
		rsInterface.scripts[0] = new int[4];
		rsInterface.scripts[0][0] = 4;
		rsInterface.scripts[0][1] = 3214;
		rsInterface.scripts[0][2] = r1;
		rsInterface.scripts[0][3] = 0;
		rsInterface.scripts[1] = new int[4];
		rsInterface.scripts[1][0] = 4;
		rsInterface.scripts[1][1] = 3214;
		rsInterface.scripts[1][2] = r2;
		rsInterface.scripts[1][3] = 0;
		rsInterface.scripts[2] = new int[4];
		rsInterface.scripts[2][0] = 4;
		rsInterface.scripts[2][1] = 3214;
		rsInterface.scripts[2][2] = r3;
		rsInterface.scripts[2][3] = 0;
		rsInterface.scripts[3] = new int[3];
		rsInterface.scripts[3][0] = 1;
		rsInterface.scripts[3][1] = 6;
		rsInterface.scripts[3][2] = 0;
		rsInterface.enabledSprite = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.disabledSprite = imageLoader(sid, "Lunar/LUNAROFF");
		Widget INT = addInterface(ID + 1);
		INT.hoverOnly = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 1, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 21, 2, INT);
		setBounds(30016, 14, 48, 3, INT);
		setBounds(rune1, 74, 48, 4, INT);
		setBounds(rune2, 130, 48, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 79, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 79, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 79, 8, INT);
	}

	public static void addLunar3RunesLargeBox(int ID, int r1, int r2, int r3, int ra1, int ra2, int ra3, int rune1, int rune2, int lvl, String name, String descr, GameFont[] TDA, int sid, int suo, int type) {
		Widget rsInterface = addInterface(ID);
		rsInterface.id = ID;
		rsInterface.parent = 1151;
		rsInterface.type = 5;
		rsInterface.optionType = type;
		rsInterface.contentType = 0;
		rsInterface.hoverType = ID + 1;
		rsInterface.spellUsableOn = suo;
		rsInterface.selectedActionName = "Cast on";
		rsInterface.width = 20;
		rsInterface.height = 20;
		rsInterface.tooltip = "Cast @gre@" + name;
		rsInterface.spellName = name;
		rsInterface.scriptOperators = new int[4];
		rsInterface.scriptDefaults = new int[4];
		rsInterface.scriptOperators[0] = 3;
		rsInterface.scriptDefaults[0] = ra1;
		rsInterface.scriptOperators[1] = 3;
		rsInterface.scriptDefaults[1] = ra2;
		rsInterface.scriptOperators[2] = 3;
		rsInterface.scriptDefaults[2] = ra3;
		rsInterface.scriptOperators[3] = 3;
		rsInterface.scriptDefaults[3] = lvl;
		rsInterface.scripts = new int[4][];
		rsInterface.scripts[0] = new int[4];
		rsInterface.scripts[0][0] = 4;
		rsInterface.scripts[0][1] = 3214;
		rsInterface.scripts[0][2] = r1;
		rsInterface.scripts[0][3] = 0;
		rsInterface.scripts[1] = new int[4];
		rsInterface.scripts[1][0] = 4;
		rsInterface.scripts[1][1] = 3214;
		rsInterface.scripts[1][2] = r2;
		rsInterface.scripts[1][3] = 0;
		rsInterface.scripts[2] = new int[4];
		rsInterface.scripts[2][0] = 4;
		rsInterface.scripts[2][1] = 3214;
		rsInterface.scripts[2][2] = r3;
		rsInterface.scripts[2][3] = 0;
		rsInterface.scripts[3] = new int[3];
		rsInterface.scripts[3][0] = 1;
		rsInterface.scripts[3][1] = 6;
		rsInterface.scripts[3][2] = 0;
		rsInterface.enabledSprite = imageLoader(sid, "Lunar/LUNARON");
		rsInterface.disabledSprite = imageLoader(sid, "Lunar/LUNAROFF");
		Widget INT = addInterface(ID + 1);
		INT.hoverOnly = true;
		INT.hoverType = -1;
		setChildren(9, INT);
		addLunarSprite(ID + 2, 2, "Lunar/BOX");
		setBounds(ID + 2, 0, 0, 0, INT);
		addText(ID + 3, "Level " + (lvl + 1) + ": " + name, 0xFF981F, true, true, 52, TDA, 1);
		setBounds(ID + 3, 90, 4, 1, INT);
		addText(ID + 4, descr, 0xAF6A1A, true, true, 52, TDA, 0);
		setBounds(ID + 4, 90, 34, 2, INT);
		setBounds(30016, 14, 61, 3, INT);
		setBounds(rune1, 74, 61, 4, INT);
		setBounds(rune2, 130, 61, 5, INT);
		addRuneText(ID + 5, ra1 + 1, r1, TDA);
		setBounds(ID + 5, 26, 92, 6, INT);
		addRuneText(ID + 6, ra2 + 1, r2, TDA);
		setBounds(ID + 6, 87, 92, 7, INT);
		addRuneText(ID + 7, ra3 + 1, r3, TDA);
		setBounds(ID + 7, 142, 92, 8, INT);
	}

	public static void setChildren(int total, Widget i) {
		i.children = new int[total];
		i.childX = new int[total];
		i.childY = new int[total];
	}

	public static void configureLunar(GameFont[] tda) {
		homeTeleport();
		constructLunar();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie", "Bake pies without a stove", tda, 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", tda, 1, 4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65, "Monster Examine", "Detect the combat statistics of a\\nmonster", tda, 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005, 66, "NPC Contact", "Speak with varied NPCs", tda, 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006, 67, "Cure Other", "Cure poisoned players", tda, 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003, 67, "Humidify", "Fills certain vessels with water", tda, 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006, 68, "Moonclan Teleport", "Teleports you to moonclan island", tda, 6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69, "Tele Group Moonclan", "Teleports players to Moonclan\\nisland", tda, 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006, 70, "Ourania Teleport", "Teleports you to ourania rune altar", tda, 8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012, 70, "Cure Me", "Cures Poison", tda, 9, 0, 5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit", "Get a kit of hunting gear", tda, 10, 0, 5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004, 71, "Waterbirth Teleport", "Teleports you to Waterbirth island", tda, 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72, "Tele Group Waterbirth", "Teleports players to Waterbirth\\nisland", tda, 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012, 73, "Cure Group", "Cures Poison on players", tda, 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74, "Stat Spy", "Cast on another player to see their\\nskill levels", tda, 14, 8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74, "Barbarian Teleport", "Teleports you to the Barbarian\\noutpost", tda, 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75, "Tele Group Barbarian", "Teleports players to the Barbarian\\noutpost", tda, 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005, 76, "Superglass Make", "Make glass without a furnace", tda, 17, 16, 2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004, 77, "Khazard Teleport", "Teleports you to Port khazard", tda, 18, 0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004, 78, "Tele Group Khazard", "Teleports players to Port khazard", tda, 19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78, "Dream", "Take a rest and restore hitpoints 3\\n times faster", tda, 20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004, 79, "String Jewellery", "String amulets without wool", tda, 21, 0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004, 80, "Stat Restore Pot\\nShare", "Share a potion with up to 4 nearby\\nplayers", tda, 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004, 81, "Magic Imbue", "Combine runes without a talisman", tda, 23, 0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82, "Fertile Soil", "Fertilise a farming patch with super\\ncompost", tda, 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83, "Boost Potion Share", "Shares a potion with up to 4 nearby\\nplayers", tda, 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004, 84, "Fishing Guild Teleport", "Teleports you to the fishing guild", tda, 26, 0, 5);
		addLunar3RunesLargeBox(30234, 9075, 563, 555, 1, 2, 13, 30012, 30004, 85, "Tele Group Fishing Guild", "Teleports players to the Fishing\\nGuild", tda, 27, 0, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010, 85, "Plank Make", "Turn Logs into planks", tda, 28, 16, 5);
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004, 86, "Catherby Teleport", "Teleports you to Catherby", tda, 29, 0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004, 87, "Tele Group Catherby", "Teleports players to Catherby", tda, 30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004, 88, "Ice Plateau Teleport", "Teleports you to Ice Plateau", tda, 31, 0, 5);
		addLunar3RunesLargeBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89, "Tele Group Ice Plateau", "Teleports players to Ice Plateau", tda, 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90, "Energy Transfer", "Spend HP and SA energy to\\n give another SA and run energy", tda, 33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91, "Heal Other", "Transfer up to 75% of hitpoints\\n to another player", tda, 34, 8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92, "Vengeance Other", "Allows another player to rebound\\ndamage to an opponent", tda, 35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance", "Rebound damage to an opponent", tda, 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94, "Heal Group", "Transfer up to 75% of hitpoints\\n to a group", tda, 37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95, "Spellbook Swap", "Change to another spellbook for 1\\nspell cast", tda, 38, 0, 5);
	}

	public static void constructLunar() {
		Widget Interface = addTabInterface(29999);
		setChildren(80, Interface);
		setBounds(30000, 11, 10, 0, Interface);
		setBounds(30017, 40, 9, 1, Interface);
		setBounds(30025, 71, 12, 2, Interface);
		setBounds(30032, 103, 10, 3, Interface);
		setBounds(30040, 135, 12, 4, Interface);
		setBounds(30048, 165, 10, 5, Interface);
		setBounds(30056, 8, 38, 6, Interface);
		setBounds(30064, 39, 39, 7, Interface);
		setBounds(30075, 71, 39, 8, Interface);
		setBounds(30083, 103, 39, 9, Interface);
		setBounds(30091, 135, 39, 10, Interface);
		setBounds(30099, 165, 37, 11, Interface);
		setBounds(30106, 12, 68, 12, Interface);
		setBounds(30114, 42, 68, 13, Interface);
		setBounds(30122, 71, 68, 14, Interface);
		setBounds(30130, 103, 68, 15, Interface);
		setBounds(30138, 135, 68, 16, Interface);
		setBounds(30146, 165, 68, 17, Interface);
		setBounds(30154, 14, 97, 18, Interface);
		setBounds(30162, 42, 97, 19, Interface);
		setBounds(30170, 71, 97, 20, Interface);
		setBounds(30178, 101, 97, 21, Interface);
		setBounds(30186, 135, 98, 22, Interface);
		setBounds(30194, 168, 98, 23, Interface);
		setBounds(30202, 11, 125, 24, Interface);
		setBounds(30210, 42, 124, 25, Interface);
		setBounds(30218, 74, 125, 26, Interface);
		setBounds(30226, 103, 125, 27, Interface);
		setBounds(30234, 135, 125, 28, Interface);
		setBounds(30242, 164, 126, 29, Interface);
		setBounds(30250, 10, 155, 30, Interface);
		setBounds(30258, 42, 155, 31, Interface);
		setBounds(30266, 71, 155, 32, Interface);
		setBounds(30274, 103, 155, 33, Interface);
		setBounds(30282, 136, 155, 34, Interface);
		setBounds(30290, 165, 155, 35, Interface);
		setBounds(30298, 13, 185, 36, Interface);
		setBounds(30306, 42, 185, 37, Interface);
		setBounds(30314, 71, 184, 38, Interface);
		setBounds(30322, 104, 184, 39, Interface);
		setBounds(30001, 6, 184, 40, Interface);// hover
		setBounds(30018, 5, 176, 41, Interface);// hover
		setBounds(30026, 5, 176, 42, Interface);// hover
		setBounds(30033, 5, 163, 43, Interface);// hover
		setBounds(30041, 5, 176, 44, Interface);// hover
		setBounds(30049, 5, 176, 45, Interface);// hover
		setBounds(30057, 5, 176, 46, Interface);// hover
		setBounds(30065, 5, 176, 47, Interface);// hover
		setBounds(30076, 5, 163, 48, Interface);// hover
		setBounds(30084, 5, 176, 49, Interface);// hover
		setBounds(30092, 5, 176, 50, Interface);// hover
		setBounds(30100, 5, 176, 51, Interface);// hover
		setBounds(30107, 5, 176, 52, Interface);// hover
		setBounds(30115, 5, 163, 53, Interface);// hover
		setBounds(30123, 5, 176, 54, Interface);// hover
		setBounds(30131, 5, 163, 55, Interface);// hover
		setBounds(30139, 5, 163, 56, Interface);// hover
		setBounds(30147, 5, 163, 57, Interface);// hover
		setBounds(30155, 5, 176, 58, Interface);// hover
		setBounds(30163, 5, 176, 59, Interface);// hover
		setBounds(30171, 5, 176, 60, Interface);// hover
		setBounds(30179, 5, 163, 61, Interface);// hover
		setBounds(30187, 5, 176, 62, Interface);// hover
		setBounds(30195, 5, 149, 63, Interface);// hover
		setBounds(30203, 5, 176, 64, Interface);// hover
		setBounds(30211, 5, 163, 65, Interface);// hover
		setBounds(30219, 5, 163, 66, Interface);// hover
		setBounds(30227, 5, 176, 67, Interface);// hover
		setBounds(30235, 5, 149, 68, Interface);// hover
		setBounds(30243, 5, 176, 69, Interface);// hover
		setBounds(30251, 5, 5, 70, Interface);// hover
		setBounds(30259, 5, 5, 71, Interface);// hover
		setBounds(30267, 5, 5, 72, Interface);// hover
		setBounds(30275, 5, 5, 73, Interface);// hover
		setBounds(30283, 5, 5, 74, Interface);// hover
		setBounds(30291, 5, 5, 75, Interface);// hover
		setBounds(30299, 5, 5, 76, Interface);// hover
		setBounds(30307, 5, 5, 77, Interface);// hover
		setBounds(30323, 5, 5, 78, Interface);// hover
		setBounds(30315, 5, 5, 79, Interface);// hover
	}

	public static void setBounds(int ID, int X, int Y, int frame, Widget RSinterface) {
		RSinterface.children[frame] = ID;
		RSinterface.childX[frame] = X;
		RSinterface.childY[frame] = Y;
	}

	public static void addButton(int i, int j, String name, int W, int H, String S, int AT) {
		Widget RSInterface = addInterface(i);
		RSInterface.id = i;
		RSInterface.parent = i;
		RSInterface.type = 5;
		RSInterface.optionType = AT;
		RSInterface.contentType = 0;
		RSInterface.opacity = 0;
		RSInterface.hoverType = 52;
		RSInterface.disabledSprite = imageLoader(j, name);
		RSInterface.enabledSprite = imageLoader(j, name);
		RSInterface.width = W;
		RSInterface.height = H;
		RSInterface.tooltip = S;
	}
	
	public static void addConfigHover(int interfaceID, int actionType,
			int hoverid, int spriteId, int spriteId2, String NAME, int Width,
			int Height, int configFrame, int configId, String Tooltip,
			int hoverId2, int hoverSpriteId, int hoverSpriteId2,
			String hoverSpriteName, int hoverId3, String hoverDisabledText,
			String hoverEnabledText, int X, int Y) {
		Widget hover = addTabInterface(interfaceID);
		hover.id = interfaceID;
		hover.parent = interfaceID;
		hover.type = 5;
		hover.optionType = actionType;
		hover.contentType = 0;
		hover.opacity = 0;
		hover.hoverType = hoverid;
		hover.disabledSprite = imageLoader(spriteId, NAME);
		hover.enabledSprite = imageLoader(spriteId2, NAME);
		hover.width = Width;
		hover.tooltip = Tooltip;
		hover.height = Height;
		hover.scriptOperators = new int[1];
		hover.scriptDefaults = new int[1];
		hover.scriptOperators[0] = 1;
		hover.scriptDefaults[0] = configId;
		hover.scripts = new int[1][3];
		hover.scripts[0][0] = 5;
		hover.scripts[0][1] = configFrame;
		hover.scripts[0][2] = 0;
		hover = addTabInterface(hoverid);
		hover.parent = hoverid;
		hover.id = hoverid;
		hover.type = 0;
		hover.optionType = 0;
		hover.width = 550;
		hover.height = 334;
		hover.hoverOnly = true;
		hover.hoverType = -1;
		addSprites(hoverId2, hoverSpriteId, hoverSpriteId2, hoverSpriteName,
				configId, configFrame);
		addHoverBox(hoverId3, interfaceID, hoverDisabledText, hoverEnabledText,
				configId, configFrame);
		setChildren(2, hover);
		setBounds(hoverId2, 15, 60, 0, hover);
		setBounds(hoverId3, X, Y, 1, hover);
	}
	
	public static void addSprites(int ID, int i, int i2, String name,
			int configId, int configFrame) {
		Widget Tab = addTabInterface(ID);
		Tab.id = ID;
		Tab.parent = ID;
		Tab.type = 5;
		Tab.optionType = 0;
		Tab.contentType = 0;
		Tab.width = 512;
		Tab.height = 334;
		Tab.opacity = 0;
		Tab.hoverType = -1;
		Tab.scriptOperators = new int[1];
		Tab.scriptDefaults = new int[1];
		Tab.scriptOperators[0] = 1;
		Tab.scriptDefaults[0] = configId;
		Tab.scripts = new int[1][3];
		Tab.scripts[0][0] = 5;
		Tab.scripts[0][1] = configFrame;
		Tab.scripts[0][2] = 0;
		Tab.disabledSprite = imageLoader(i, name);
		Tab.enabledSprite = imageLoader(i2, name);
	}
	public String[] tooltips;
	public boolean newScroller;	
	@SuppressWarnings("unused")
	private int mOverInterToTrigger;
	@SuppressWarnings("unused")
	private boolean interfaceShown;
}
