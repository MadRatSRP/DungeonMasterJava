--------------------------------
Dungeon Master Java Version 1.05
--------------------------------
August 14, 2002


About
-----
DM Java is a remake of the classic game Dungeon Master by FTL/Software Heaven. It is not an
exact clone, but retains most of the same gameplay. It is coded entirely in Java, and is
therefore able to run on any platform supporting Java version 1.4 with an installed runtime
environment (virtual machine). It is designed to run in 800x600 or greater desktop resolution
with 16 bit high color or better. The editor should be run at 1024x768 or greater resolution.

Changes
-------
v1.05
      - Fullscreeen support (see dmnew.cfg file).
      - Brightness booster.
      - Keyboard fixes for the new JRE 1.4.
      - Fixed event squares setting choices visible/invisible.
      - A few other minor bug fixes.

Compatability
-------------
This version is compatible with all dungeons and most saved games from 1.04.
Due to a certain bug fix, in some rare cases a saved game from 1.04 will fail to load. This
only happens if the game was saved while a non-reusable switch was delaying or resetting.
 - Loading the game in 1.04 and waiting a while, then saving again may fix it (basically
   waiting until the delay/reset has finished). Otherwise, continue to use 1.04 or start
   over.


Usage
-----
Assuming a VM is installed, the game can be run from a command prompt by typing "java dmnew"
without the quotes. (This assumes the java executable is in the system path.) The game may
also be run without the command prompt (in Windows, anyway) by setting up a shortcut with
"javaw.exe dmnew" as the target.
The editor can be run similarly but substituting "DMEditor" for "dmnew" in the above.
For more detailed instructions, see the included "install.txt" file.

Background
----------
The game is a sequel to Dungeon Master (and Chaos Strikes Back), by FTL/Software Heaven. For
their storylines, you can see their manuals (available online -> see links below).

In Dungeon Master, a great wizard named the Grey Lord was searching for the Power Gem, a
magical artifact that thawed the ice of creation and freed the races. He finds it, but makes
a mistake when trying to obtain it with his Firestaff. The end result is he was split into 2
pieces, 2 separate beings: Lord Order and Lord Chaos. The evil and insane Chaos sought to
destroy the world. The Grey Lord's apprentice, Theron (the player), went into Mount Anaias
to stop him. Theron had also been affected by the mistake, and no longer had a physical body.
To stop Chaos, he had to use champions saved from imprisonment in mirrors. The champions went
through the dungeon, obtained the Firestaff and joined it with the Power Gem. The resulting 
Staff had the power to put Chaos and Order back together. This was done by surrounding Chaos 
with Fluxcages and then Fusing him. Balance was restored to the world.

In CSB, Chaos attempted to return to the world by using Corbum Ore mined from a Ful Ya Pit.
He gathered a new army of demons and monsters in a secret dungeon. Yet his plans were foiled, 
as the champions defeated him once again by destroying the 4 pieces of Ore.

This new quest takes place about 1000 years after the time of CSB. See the intro animation
for the basic storyline.

Gameplay
--------
Gameplay is much like the original. Movement must be done with the keyboard, as there are no
onscreen arrow buttons. The following keys are used:
        Movement:
        7,u -> turn left 90 degrees
        9,o -> turn right 90 degrees
        8,i -> move forward one square
        5,k -> move back one square
        4,j -> sidestep left one square
        6,l -> sidestep right one square
        
        Function keys F1-F4 access champion inventories
        ESC pauses the game
        F9 brings up the option menu
        F5 quick save (overwrites last saved game, shows dialog if there isn't one yet)
        F7 quick load (loads last saved game, shows dialog if there isn't one yet)
The mouse is used for all other activities, just as in the original game.
See the manual for the original game for more details.

Editor
------
The editor is capable of building complete maps from scratch, modifying existing maps, and
editing saved games. Documentation is available in html format. See the DM Java Homepage link
below.

Spell List
----------
Here is a list of spells to start the game with. There are some others but you will have to
experiment or find scrolls in the dungeon to learn them. For more info on spells, see one of
the web pages about the original games below.
Priest
  Health Potion - VI
  Stamina Potion - YA
  Mana Potion - ZO BRO RA
  Antidote - VI BRO
  Strength Potion - FUL BRO KU
  Dexterity Potion - OH BRO ROS
  Vitality Potion - OH BRO NETA
  Intelligence Potion - OH BRO DAIN
  Wisdom Potion - YA BRO NETA
  Defense Potion - YA BRO
  Magic Resist Potion - ZO BRO DAIN
  Magic Vision - OH EW RA
Wizard
  Magic Torch - FUL
  Open Some Doors - ZO
  Dispell Non-Material - DES EW
  Fireball - FUL IR
  Poison Cloud - OH VEN
  Poison Bolt - DES VEN
  Lightning - OH KATH RA
  Ven Bomb - ZO VEN
  Plasma - ZO KATH RA

Links
-----
Dungeon Master Java Homepage - http://www.cs.pitt.edu/~alandale/dmjava
	- News, latest version and further information

Dungeon Master Java Message Board - http://pub54.ezboard.com/bdungeonmasterjava
	- Latest news, bug reports, questions, comments, etc.

Dungeon Master and Chaos Strikes Back Encyclopedia - http://dmweb.free.fr
	- Info and manuals for the original games
        
Java Homepage - http://java.sun.com
        - Sun's Java page, with free downloadable virtual machine's and current Java news
        
Finally
-------
This game is freeware. Use at your own risk.
It would be nice if people send me any dungeons, items, monsters, or graphics they create
for it. That way, I can have more fun with the game myself! And please, visit the message
board and give me suggestions for future versions and report any bugs you find.
I hope you enjoy it.

 - Alan Berfield (alandale@hotmail.com)