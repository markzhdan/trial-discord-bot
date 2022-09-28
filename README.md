# DiscordBot-MorvenSMP
Discord bot for Morven SMP discord server

# Purpose
This bot keeps track of a player's trial period and automatically polls existing members to decide if the player should join the server on a permanent basis.


# Features
1. Connects with MongoDB to create, store and update member data.

2. Automatic member voting system and results.
    - To start a trial period, assign the "trial" role to a user.

3. Control over the duration of trial and voting length.

4. Displays the date that a user joined the server on.

5. Admin control.


# Commands
**!triallength [x]**
- Sets the length of the trial

**!votelength [x]**
- Sets the length of the voting period

**!vote [@user]**
- Overrides trial length and instantly start vote on an existing player

**!joined**
- Displays the join date of the user
