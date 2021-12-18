# Ai assisted player for a two-player-game
Here the game is Oware. <br />
This project has been realized during an AI Game Programming course, by Antoine Cousson and myself <br />

## The game: 
Oware is a zero-sum african game. <br />
It is originally played on a 12 holes board, 6 by 6 and with 4 seeds by hole. <br />

## The rules:
    - when you play a hole you take all seeds in it
    - the sowing is determine at the beginning as clockwise or anti-clockwise
    - during the sowing, you put one seed into each following the one you start from
    - if you have enough seeds to return to your starting hole, you don't sow it.
    - if the **last** hole you sow contains 2 or 3 seeds **after** you sow it, you take the seeds in it. <br />
    Then if the precedent as the same situation, 2 or 3 seeds you take his seeds as weel and you go until you find <br />
    a hole that does not match the requirement
    - game stops when one of the player have more than half the seeds or when one is starved.

## The project:
The project was to develop an AI assisted player for a modified version of Oware.<br />

### The player:
    - the algorithm is an Minimax with Alpha-Beta pruning
    - the basic evaluation function was the difference in score between players
    - the evaluation is always based one the first player
   
### New rules:
    - the board is a 16 holes board, 8 by 8
    - player one got the odd holes
    - player two got the even holes
    - there are two colours of seeds, blue and red. There are 2 of both colours in each holes at the beginning
    - Blue seeds can be sow only in opponent holes
    - red seeds can be sow in all holes
    - when you play a hole you only play one colour, blue or red
    - game stops when one of the player have more than half the seeds or when one is starved <br />
    or when there's less than 8 seeds remaining on the board.
    - if starving and less than 8 happens at the same time, starving is prior
    

