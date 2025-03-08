# CS3500
# Three-Trios
**OVERVIEW**
An implementation of the game Three-Trios (an Othello kind of game).
This is a 2 player card game (red and blue), where players take turns placing cards on a grid and battling to win the color of the cards.  The winner of the game has the majority of grid spaces filled with their color. Each card has 4 numbers in each cardinal direction, and 2 cards battle by comparing the number in the direction facing each other.  The winning player gets the other player's card changed to the winner's color, and the game continues.  

This game follows the model, view, controller organizational programming, which allows for better encapsulation and 
seperation between classes.  The majority of our logic lives in the model: the game state tracking, grid representation, player representation, and management of legal moves within the rules of the game.  The Grid is the game board, and tracks positions of the cards placed, the open tiles "C" and the tiiles with holes "X".  The GameState and Player Color are also represented as an Enum.  The Player class stores the data for the game and helps manage the hands of each player. The Model class controls the smaller portions of our game, such as the Grid, Player, Tile, and PlayCard. 

The potential extendability of this code: 
This game could being a GUI (graphic user interface) instead of the current visual output the game runs on.  It also can be extended to have different modes of the game, or rather, extensions of the rules of the game.  Since the Player methods are seperated into a seperate class and based off of the two colors, the game could be extended to have more players, or even have one player against a computer.  

SOURCE ORGANIZATION: 
This game follows Model-View-Controller (MVC) architecure. 
- the model contains the game logic, without any actual application.  Becuase of this, our model contains
our grid, player cards, players, tiles, and more; basically the parts necessary to build the game. 
- the controller is where our computer players live, as well as the application of our model's parts
- the view was originally a console view, but as of now, has been upgraded to a graphic user interface.

                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Our game contains a **File Reading System**:: 

GRID READER:
The first one is a MapReader, which is able to read the Grid file formats.  Its inside of TriosReaderImpl class and implements the MapReader<PlayCard> interface,  It creates a 2D array of Tile objects to make the board layout for the ThreeTrios game.  It reads the rows and columns, and has 2 types of cells, the open tiles "C" and the tiles with holes "X".
Grid Configuration file : The file’s format is as follows
ROWS COLS
ROW_0
ROW_1
ROW_2
...

public Tile<PlayCard>[][] read(File file) --> this is the actual reader itself

it would be used like :
File gridFile = new File("4x4grid.config");
TriosReaderImpl reader = new TriosReaderImpl();
Tile<PlayCard>[][] gameBoard = reader.read(gridFile);


If the file received is in neither of these formats, the program will throw an IllegalArgumentException.

CARD READER: 
The card reader reads the card configurations from files and allows them to be converted into card objects.  It checks the validity of the input, and converts valid file data into PlayCard Objects.  Invalid input (such as Nulls or bad file path) will throw an Invalid Argument Exception. 
Card Database File : This file’s format is a list of cards
CARD_NAME NORTH SOUTH EAST WEST
CARD_NAME NORTH SOUTH EAST WEST
CARD_NAME NORTH SOUTH EAST WEST
...
The cards have their Compass Value (NORTH SOUTH EAST WEST) and their values (1,2,3,4,5,6,7,8,9,A) represented as Enums and PlayCard to put the values and directions together. The order of directions is a fixed value .  The most important line of code in this reader is 
public List<PlayCard> read(File file)  --> this is the actual reader itself

it would be used like this : 
CardReader reader = new CardReader();
File configFile = new File("path/to/deck.config");
List<PlayCard> deck = reader.read(configFile);

                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                                                **Map of Game**
Within cs3500/threetrios/
within model : 
- ThreeTrioModel = this is the main game model, where majority of our methods live
- CardReader = this is the reader for our cards and deck
- TriosReaderImpl = this is the reader for our grid and tiles
- Grid = our game board
- Title = the cells within the game board/ grid
- Player = holds the player state and legal actions
- PlayCard = the implementation of cards
- Value = the valid card values (0-9, A)
- Compass = The directions for the cards, NORTH SOUTH EAST WEST
- PlayerColor = defines the two player's colors
- GameState = holds the state of the game, whether its in progress / battle or over.
within view : 
- ThreeTrioConsoleView = Implements a view for Three Trio viewable on the console line
- ThreeTrioView = interface representation of the view of a ThreeTrios Game.
within test :
- CardReaderTest
- Grid Test
- Model Test
- PlayCardTest
- Player Test
- Tiles Test
- TrioReadertest
- View Test
- StrategyTest
A feature of our testing suite is we used a mock for testing the view, to promote isolation and to have a controlled
environment for testing.  

Strategy
-
Our game has a few different classes that examine the board with a specific bias in mind (strategy) and picks their idea of best move.
To Implement
- Move = as data class made to hold all the information a Strategy wants to return, a handIndex, x and y positions, and for the future a score to compare.
- TieBreaker = and Comparator for Move it will compare a Move first by its proximity to the top of the board, then how left it is, and finally its position in the hand relative to the other. This class lets you quickly sort a list of Moves of equal value by the tie breaker rules. We did this because TieBreaker helps translate an Strategy to an InfallableStrategy and thus if Tie breaking ever changed it be an easy thing to change.
- Strategy an interface that represents imperfect strategies, which means it can return one, many or no moves. Those that implement this interface will always return a list of Moves that all have the same ranking as "best move" in the strategies logic
- InfallableStrategy is a strategy that will alway return a single move, and will never be a null move. 
- SingleMove = is an implementation of InfallableStrategy that takes a Strategy and the list it produces and picks a single Move from it using TieBreaker's logic. If the list is empty it will call on an InfallableStrategy in this case UpperLeftStrategy to ensure a non null move is made.
- UpperLeftStrategy = is an implementation of an InfallableStrategy, there is no situation in a game where there shouldn't be a space to play a piece and someone can play, thus this will always return a non-null move.
- CornerStrategy = a Strategy implementation that will go for the corners and try to place its hardest to flip card (defined in method) in that spot.
- FlipMaxStrategy = a Strategy implementation that will check every space with every card and give a list of the highest Moves it can make.

Extra Credit
- 
Implemented Strategy 3 where the bias is towards having the least flippable cards when compared to another player's hand
- You can find the implementation under the class name LeastFlipableStrategy
- the test is in StrategyTest starting @ line 187
- The interesting behavior I noticed while implementing was in the early game this strategy heavily mimics the Corner Strategy

Implemented a TwoStrategies class that allows someone to stack multiple  strategies together, this let me uncouple the strategies and its implementation can be found in the name given and tests begin @ line 299

                       ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  
**QUICKSTART**
  To run the game, we have the start of a main method, which goes through a preset implementation of the view and model features in the class ThreeTrios.  

public class ThreeTrios {

  /**
   * Builds and starts a Three Trio Game.
   * @param args unimportant for now.
   */
  public static void main(String[] args) {
    //Where actual game will be played.
    TrioModel<PlayCard> model = new ThreeTrioModel();
    ThreeTrioView view = new ThreeTrioConsoleView(model);

public class ThreeTrios {
  public static void main(String[] args) {
  ThreeTrioModel model = new ThreeTrioModel();
  model.initializeGame(
  "HoleButReachable.config", "AllDeck.config", false);

    ThreeTriosGUI view = new ThreeTriosGUI(model);
    view.addClickListener(new ControllerImpl(model, view));
    view.makeVisible();
}
}

---Changes for part 2----
We updated our model interface to expose more functionality for easier use this also revealed numerous bugs in our earlier implementation
- The Model is now able to tell how many tiles a card will flip instead of only finding out through the battle step.
- The Model's Read Only section now includes a Grid's width and height as it felt too tedious to .getGrid.getHeight()
- Reworked our model's method signatures: Grid, Tile now both have interfaces above them so, we aren't limited to specific implementation for future use.
- Through creating an interface called Cell for Tile we discovered that our getGrid was sending a shallow copy, which doubtless saved us alot of heartache for the implementation of strategies no doubt.
- No methods were added to TrioModel buts to its ReadOnly Parent we added getGridHeight(), getGridWidth(), getFlipTotal(), getScore(), all to make acessing information easier for our other modules.

We  had to update our main method (ThreeTrios) to run with the new GUI View rather than
the old console view.

We also moved our reader classes into the controller package, rather than it being in the model
as it was before.  Because of this, we moved the tests for the readers into the controller test
package to keep organized.


Our GUI setup has multiple parts:
- everything is contained to our interface, TriosViewGUI, which guarantees that our
  view is taking in the ReadOnlyTrioModel to prevent accidental mutation.
  -We have 2 main panel classes, GridPanel and HandPanel, but we have 3 panels in implementation.
  the GridPanel renders our board based on the config files given to the model, while the HandPanel
  creates the 2 panels on either side of our grid, containing each player's hands.
  -ThreeTriosGUI is our implementation of the view, and contains the majority of JFrame methods
  putting together the window, panels, and frame.
  -our main class then uses the ThreeTriosGUI method to create an instance of the game.

The coordinate system on our grid has an index of 0,0 starting at the upper left hand corner.

---Changes For Part 3---

IPlayer
-
Our original design for a player was limited to holding its cards and coloring new cards. With the requirements of the project we decided to redesign how we tackled this.
- We removed Player and replaced it with an interface called IPlayer that takes the methods of our original Player and adds addListener() and callMove() to accommodate our new functionality requirements.
- Since the original Player itself worked well regardless if it was a human or machine, which we tested in our previous iteration we felt safe in isolating the previous methods in an Abstract class called AbstractPlayer, which was a one for one copy of the original Player methods. This let us keep the code we wrote with assurance that it'll still work with our model.
- Finally, we created two classes that extend off AbstractPlayer that defines the current two states of player HumanPlayer and MachinePlayer. MachinePlayer acts as a decorator of Strategy and uses it method for callMove() which activates our listeners.


Observers
-
We built our two interfaces to deal with PlayerAction and ModelWatcher, which act as our key listeners for the controller to listen to specific actions in our model and Players. The controller implements these contracts so it can listen to these events.
- PlayerAction defines the ways a Player (human or not) can interact with the game, either through the GUI in the case of a human with the methods handleCardClick() and handleCellClick(), or with a non-human player that plays through the model.
- ModelWatcher listens for when a turn is changing so the controller knows to weather to allow its player to play the game and to call a winner for the end screen.

Controller
- 
Our controller now takes in a view, player, and model and acts as a middle man for interactions between the processes.
The controller will bound check the player, i.e it will give Dialog Messages of illegal plays and properly maintain turn order.
It announces the winner and final score, ensures the views update asynch with each input from the players.

Main
-
We rebuilt our main so that it can take in configuration files and it displays two views.

*******

11/23 Updates ~
from homework 2, we had to update our controller tests to now properly accomodate to our observer/
listener.  In the model we added a game state observer, game state log, move history,
and move record.
Game State Observer: helps detatch the game state changes from the reactions to those changes.
follows the observer pattern and further helps seperate components.
Game State Log: Tracks game flow and helps make a trail of game actions.
Move Record: makes it easier to find all info about a specific move in one place added this because
we needed help with debugging, and moving forward helps us know what the strategy players
are doing directly.

<<<<<<<<<<<<<< most of these were added for debugging purposes but I left them in the final vers.
of the game.

We wrote aditional tests in the ControllerImpTest class, and wrote aditional tests for our
Game State Observer, machine player, and a mockstrategy test model.

Command Line Arguments
-
To configure the game with Command Line arguments the pattern is as follows

- map deck redPlayer bluePlayer

For example, the config 3_3Grid BasicDeck human strategy1, will create a Three Trios game with a 3x3 grid a Basic Deck with the red player being a human and blue player using strategy1.

The following strategies are defined below
- Strategy1: Will try to flip the most amount of cards
- Strategy2: Will try to play the corners
- Strategy3: Will try to play cards that won't be flipped.


<img width="645" alt="Screenshot 2025-03-08 at 10 09 39 AM" src="https://github.com/user-attachments/assets/b9d22028-7492-4afd-8ed1-5c4bbe57d64d" />

                        

