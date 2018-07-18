import java.util.Scanner;

import java.util.concurrent.ThreadLocalRandom;
public class Main {

	static int playerHp = 100, monsterHp = 100;
	static double playerDf = 0.2, monsterDf = 0;
	static double playerLuck = 1;
	static int potions20 = 2;
	static int attackMin = 15, attackMax = 20;
	static int monsterAttackMin = 7, monsterAttackMax = 14;
	static boolean monsterAppears = false;
	static double attackMultiplier = 1;
	static int playerDamage, monsterDamage;
	static int timeMin = 0, timeHr = 6;
	static String time;
	static int turnCounter = 0;
	static boolean playing = true;
	static boolean inTown = false;
	static int gold = 0;
	static double monsterDifficulty = 1;
	static double goldEarned;
	static double fleeProb;
	static boolean flee = false;
	static int innFee;
	static double bankGold;
	static int timePassed = 0;
	static int mapRows = 25, mapCols = 25;
	static String[][] map = new String[mapRows][mapCols];
	static int playerRow = 5, playerCol = 5;
	static int questNumber = 0;
	public static void main(String args[]) {
	
			//Town.test();
			mapInitialize();
			
			//monsterBattle(false);
			mapPrint();
			while(playing == true) {
				if(inTown == false) {
					explore();
				}
				else if(inTown == true) {
					town();
				}
			}
	}
	
	
	public static int rand(int min, int max) {
	    int randomNumber;
	    int lastRandomNumber = 0;
	    do {
	        randomNumber = ThreadLocalRandom.current().nextInt(min,max + 1);
	    } while (randomNumber == lastRandomNumber);
	    lastRandomNumber = randomNumber;
	    return randomNumber;
	}
	public static double randDouble(double min, double max) {
	    double randomNumber;
	    double lastRandomNumber = 0;
	    do {
	        randomNumber = ThreadLocalRandom.current().nextDouble(min,max + 1);
	    } while (randomNumber == lastRandomNumber);
	    lastRandomNumber = randomNumber;
	    return randomNumber;
	}
	public static void wait(double seconds) {
		int milliSeconds = (int) (seconds * 1000); 
		try {
			Thread.sleep(milliSeconds);
		}
		catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	
	}
	public static boolean monsterBattle(boolean flee) {
		if(flee == false) {
			System.out.println("Enemy Approaches, His Stats Are: \nHealth: " + monsterHp + "\nDefense: " + monsterDf);
			
		}
		
		while (playerHp > 0 && monsterHp > 0) {
			System.out.println("What will you do? \n1: Attack\n2: Defend\n3: Use Item\n4: Flee");
	
			Scanner scBattleChoice = new Scanner(System.in);
			int choice = scBattleChoice.nextInt();
			if(choice == 1) {
				
				monsterDamage = rand(monsterAttackMin, monsterAttackMax);
				playerDamage =  rand(attackMin, attackMax);
				monsterHp -= playerDamage * attackMultiplier;
				playerHp -= monsterDamage;
				System.out.println("You lost " + (int) monsterDamage + " health ||| Your enemy lost " + (int) (playerDamage * attackMultiplier) + " health.");
				attackMultiplier = 1;
				if(playerHp > 0 && monsterHp > 0) {
					System.out.println("Enemy's Health: " + monsterHp);
					System.out.println("Your Health: " + playerHp);
				}
			}
			else if(choice == 2) {
			    monsterDamage = rand(monsterAttackMin, monsterAttackMax);
				playerHp -= monsterDamage - (playerDf * monsterDamage);
				monsterHp -= (playerDf * monsterDamage);
				System.out.println("You lost " + (int) (monsterDamage - (playerDf * monsterDamage)) + " health ||| Your enemy lost " + (int) (playerDf * monsterDamage) + " health.");
				attackMultiplier += .5;
				System.out.println("Enemy's Health: " + monsterHp);
				System.out.println("Your Health: " + playerHp);
				
			}
			else if(choice == 3) {
				if(potions20 > 0) {
					System.out.println("What item would you like to use?\n1: 20HP Potion\n2: Cancel");
					Scanner scPotionChoice = new Scanner(System.in);
					int potionChoice = scPotionChoice.nextInt();
					if(potionChoice == 1) {
						playerHp += 2000;
						System.out.println("You have been healed, your health is now: " + playerHp);
					}
					else if(potionChoice == 2) {
						
					}
					
				}
				else {
					System.out.println("You don't have any potions.");
				}
			}
			else if(choice == 4) {
				fleeProb = randDouble(1, 4 - (playerLuck / 10));
				System.out.println(fleeProb);
				if(fleeProb > 1 && fleeProb <= 2) {
					System.out.println("You have sucessfully escaped.");
					//explore();
				}
				else if(fleeProb > 2 && fleeProb < 5) {
					System.out.println("You have tried to escape and failed. You've lost " + (int) rand(monsterAttackMin, monsterAttackMax) + " health.");
					playerHp -= rand(monsterAttackMin, monsterAttackMax);
	
				}
			}
			turnCounter += 1;
	
		}
		monsterHp = 100;
		attackMultiplier = 1;
		goldEarned = rand((int) (5 * monsterDifficulty), (int) (20 * monsterDifficulty));
		gold += goldEarned;
		if(playerHp < 1) return false;
		else return true;
	
	}
	public static String time() {
		timeMin = turnCounter * 20;
		timePassed = turnCounter * 20;
		if(timeMin >= 60) {
			timeHr += timeMin / 60;
			timeMin = timeMin % 60; 
	
		}
		if(timeHr > 24) {
			timeHr = 0;
		}
		else if(timeHr < 10) {
			time = "The time is: 0" + timeHr + ":" + timeMin;
		}
		if(timeMin == 0) {
			time = "The time is: " + timeHr + ":" + "00";
		}
		else {
			time = "The time is: " + timeHr + ":" + timeMin;
		
		}
		return time;
	}	
	public static boolean explore() {
		System.out.println("What will you do?\n1: Explore\n2: Go to town");
		Scanner scExploreChoice = new Scanner(System.in);
		int exploreChoice = scExploreChoice.nextInt();
		if(exploreChoice == 1) {
			mapMove();
		}
		else if(exploreChoice == 2) {
			inTown = true;
		}
		/*if(monsterBattle(false) == true) {
			System.out.println("Congratulations, you won!");
			System.out.println("You've earned " + goldEarned + "g");
			System.out.println(time());
		}
		else {
			System.out.println("You lost");
		}*/
		return true;
	}
	public static void mapInitialize() {
		
		for(int i = 0; i<mapRows; i++)
		{
		    for(int j = 0; j<mapCols; j++)
		    {
		    	map[i][j] = "* ";
		    	
		    }
		}
		map[(mapRows - 1) / 2][(mapCols - 1) / 2] = "@ ";
		map[rand(0, mapRows)][rand(0, mapCols)] = "# "; map[rand(0, mapRows)][rand(0, mapCols)] = "# "; map[rand(0, mapRows)][rand(0, mapCols)] = "# "; map[rand(0, mapRows)][rand(0, mapCols)] = "# ";
		map[rand(0, mapRows)][rand(0, mapCols)] = "% "; map[rand(0, mapRows)][rand(0, mapCols)] = "% "; 
		map[rand(0, mapRows)][rand(0, mapCols)] = "$ ";
		map[rand(0, mapRows)][rand(0, mapCols)] = "? ";
	
	}
	public static void mapPrint() {
		map[playerRow][playerCol] = "@ ";
		for(int i = 0; i<mapRows; i++)
		{
		    for(int j = 0; j<mapCols; j++)
		    {
		    	System.out.print(map[i][j]);
		    }
		    System.out.println();
		}
	}
	public static void mapMove() {
		mapPrint();
		System.out.println("What direction would you like to go\n1: North\n2: East\n3: South\n4: West");
		Scanner scMapChoice = new Scanner(System.in);
		int mapChoice = scMapChoice.nextInt();
		if(mapChoice == 1) {
			mapArea("North");
			playerRow -= 1;
			map[playerRow + 1][playerCol] = "* ";
		}
		else if(mapChoice == 2) {
			mapArea("East");
			playerCol += 1;
			map[playerRow][playerCol - 1] = "* ";
		}
		else if(mapChoice == 3) {
			mapArea("South");
			playerRow += 1;
			map[playerRow - 1][playerCol] = "* ";
		}
		else if(mapChoice == 4) {
			mapArea("West");
			playerCol -= 1;
			map[playerRow][playerCol + 1] = "* ";
	
		}
			
	}
	public static void mapArea(String area) {
		if(area.equals("North")) {
			if(map[playerRow - 1][playerCol].equals("% ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("# ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("$ ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("* ")) {
				if(rand(1, 4) == 1) {
					monsterBattle(false);
				}
			}
			else if(map[playerRow - 1][playerCol].equals("? ")) {
				mapQuest(questNumber);
			}
		}
		else if(area.equals("East")) {
			if(map[playerRow][playerCol - 1].equals("% ")) {
				
			}
			else if(map[playerRow][playerCol - 1].equals("# ")) {
				
			}
			else if(map[playerRow][playerCol - 1].equals("$ ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("* ")) {
				if(rand(1, 4) == 1) {
					monsterBattle(false);
				}
			}
			else if(map[playerRow - 1][playerCol].equals("? ")) {
				mapQuest(questNumber);
			}
		}
		else if(area.equals("South")) {
			if(map[playerRow + 1][playerCol].equals("% ")) {
				
			}
			else if(map[playerRow + 1][playerCol].equals("# ")) {
				
			}
			else if(map[playerRow + 1][playerCol].equals("$ ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("* ")) {
				if(rand(1, 4) == 1) {
					monsterBattle(false);
				}
			}
			else if(map[playerRow - 1][playerCol].equals("? ")) {
				mapQuest(questNumber);
			}
		}
		else if(area.equals("West")) {
			if(map[playerRow][playerCol + 1].equals("% ")) {
				
			}
			else if(map[playerRow][playerCol + 1].equals("# ")) {
				
			}
			else if(map[playerRow][playerCol + 1].equals("$ ")) {
				
			}
			else if(map[playerRow - 1][playerCol].equals("* ")) {
				if(rand(1, 4) == 1) {
					monsterBattle(false);
				}
			}
			else if(map[playerRow - 1][playerCol].equals("? ")) {
				mapQuest(questNumber);
			}
		}
			
	}
	public static void mapBoss() {
		
	}
	public static void mapQuest(int questNumber) {
		
	}
	public static void town() {
		System.out.println("Where would you like to go?\n1: Inn\n2: Bank\n3: Shop\n4: Guild House\n5: Leave");
		Scanner sctownChoice = new Scanner(System.in);
		int townChoice = sctownChoice.nextInt();
		
		//Inn//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		if(townChoice == 1) {
			innFee = rand((int)(10 - playerLuck * 2),(int) (25 - playerLuck * 2));
			System.out.println("You have " + gold + "g");
			System.out.println("Would you like to stay at the inn until morning for " + innFee + "g");
		}
		//Bank////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if(townChoice == 2) {
			if(bankGold != 0) {
				bankGold += timePassed * 0.01;
			}
	
			System.out.println(timePassed);
			System.out.println("You have " + gold + "g. ||| There is " + (int) bankGold + "g in the bank.");
			System.out.println("What would you like to do?\n1: Withdraw\n2: Deposit");
			Scanner scBankChoice = new Scanner(System.in);
			int bankChoice = scBankChoice.nextInt();
			if(bankChoice == 1) {
				System.out.println("How much would you like to withdraw?");
				Scanner scWithdrawAmt = new Scanner(System.in);
				int WithdrawAmt = scWithdrawAmt.nextInt();
				if(WithdrawAmt > bankGold) {
					System.out.println("There is not enough gold in the bank.");
				}
				else {
					System.out.println("You have withdrawn " + WithdrawAmt + "g.");
					System.out.println("There is " + bankGold + "g left in your account.");
					
				}
			}
			else if(bankChoice == 2) {
				System.out.println("How much money would you like to deposit?");
				Scanner scDepositAmt = new Scanner(System.in);
				int DepositAmt = scDepositAmt.nextInt();
				
				
				if(DepositAmt > gold) {
					System.out.println("You don't have enough gold to do that.");
				}
				else {
					bankGold += DepositAmt;
					gold -= DepositAmt;
				}
			}
			
		}
		//Shop///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if(townChoice == 3) {
			
		}
		//Guild House/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if(townChoice == 4) {
			
		}
		//Leave///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		else if(townChoice == 5) {
			inTown = false;
		}
	}
}