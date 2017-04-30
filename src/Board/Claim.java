package Board;

public class Claim {
	// A "claim" is a tile on the board of the game, which is 6x6
	// Players can claim these lands (which may have certain strategic advantages, such as food, etc.)
	// If someone captures every tile they win
	// A war for a tile - two groups arrive at the same tile and decide to declare war on each other (at a state of war)
    // If troops challenge the current state, power will be determined (ex. 5 and 4) and then a random number will find the difference if it is too close
    int status;
    Claim(int status) {
        this.status = status;
    }
    //Statuses - 0 = unclaimed, 1 = toucan, 2 = parrot, 3 = macaw, 4 = eclectus

}
