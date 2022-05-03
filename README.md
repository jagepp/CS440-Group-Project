# CS440-Group-Project
Contributors:  Jay Podapati, Bhumit Patel, Michael Ahmadi, Jacob Gepp

Demo Completed April, 2021
Created in Using Maven and JavaFX in Eclipse IDE

1. Project Description
Battleship Legends is a modification of the classic board game Battleship with
additional functionality and features to enhance game quality. There are many
fundamentals classes used in this project : Player, Ship, Point, Weapon, HitDetection,
Power-ups, Map.
The product was inspired by the “BattleShip Legends of Group 2 Fall 2020,” and
strived to visualize the product based on it’s requirements.

2. Project Deliverable
Battleship Legends is a Java application with three completed gamemodes. There
is Multiplayer (Local), Multiplayer (Online), and Singleplayer. It uses GUI’s created with
JavaFX to display the game. Multiplayer (Local) and Multiplayer (Online) both allow the
game to be played with two human players, while in the Singleplayer gamemode a
human player plays against a simple AI. Each game mode has the same core
functionality: the ability to place ships, move ships, fire at enemy ships, and gather points
to spend in the shop for powerups and weapons.
For the first release, only the Multiplayer (Local) gamemode was completed.
Included in this gamemode was core functionality such as placing ships, moving, firing,
and ending turn.
For the second release, we added the Multiplayer (Online) and Singleplayer
gamemodes, points functionality, and some other minor improvements.
In regards to the original design document, the Singleplayer and Multiplayer
gamemodes, the points system, and the turn-based moving and firing are similar.
Randomly generated world events and scaled up extensions of the current functionality
are features of the design document that were left out of our final product.

3. Testing
The Ship Movement test checked for the movement of the ship in forward or
backward directions and the rotation of the ship. The Hit Detection test checked the firing
functionality of a battleship that is used when a player fires at a point that the opponent's
ship might be on.
For single-player functionality, the AI subclass of the player class needed to be
tested. The constructor needed to overwrite the default values of the player class as some
entries are different. The AI class also had an additional function, nextShot, that selected
the next shot the AI will take based on the results of the previous shot. If the previous
shot hit the enemy’s ship, it must return the same point as the previous. If the previous
shot missed, the AI must be able to consistently pick a location to shoot at that is within
the game board bounds.
Tested the functionalities of different weapons that a player can buy in order to
increase his chance of winning. The Torpedo test checked whether the 8 surrounding
valid points were in the torpedo weapon’s attack range. The Horizontal Line Gun Test
checked whether all the points in the row of the fired point were in the gun’s range. The
Vertical Line Gun Test checked whether all the points in the row of the fired point were
in the gun’s range. The Double Shot Gun test checked whether the gun can shoot up to
twice per turn, and only twice.

4. Inspection
The inspection of the online multiplayer mode focuses on matching the
functionality of the multiplayer local mode. This includes making the player wait their
turn, allowing ship movement and firing with GUI feedback, and the ability to acquire
and spend points at the shop. The server window should accurately display the player’s
actions in real time, indicating that both players have the most recent game information.
The inspection of functions relating to moving the ship focuses on the
functionality of the methods, and the readability of the code. A thorough inspection took
place to ensure each method operated exactly as it was designed, and without any errors.
The code was also inspected in regards to the naming of functions, naming of variables,
recycling of code, and complexity of how it was written.
5. Recommendations and Conclusions
As of April 24, 2021, all items covered have passed their testing and inspections.
Any and all bugs uncovered during the testing and inspection phase have been addressed
and fixed in the current version of the game. Further tests and inspections will be needed
to reveal additional bugs/oversights.

6. Project Issues
At the beginning of the project the group faced certain difficulties such as
imbalance of work among group members due to an improper division of the system into
subsystems. The group did not work in an organized manner. However, as the semester
progressed the work was more evenly distributed and due to an increased clarity of the
project were able to divide the system into components in an organized way. A possible
improvement would be to focus more on object and system design in the initial phase of
the project, since the lack of our focus on object design resulted in the main application
class having way too many lines of code
