package lv.cliquant.votinglevels.objects;

import lv.cliquant.votinglevels.VotingLevels;

import java.util.List;

public class Level {
    private int requiredVotes;
    private List<String> rewardText;
    private List<String> commands;

    private int level;

    public Level(int level, int requiredVotes, List<String> rewardText, List<String> commands) {
        VotingLevels.get().getLogger().info("Creating new Level object");
        this.level = level;
        this.requiredVotes = requiredVotes;
        this.rewardText = rewardText;
        this.commands = commands;
    }

    public int getRequiredVotes() {
        return requiredVotes;
    }

    public List<String> getRewardText() {
        return rewardText;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getLevel() {
        return level;
    }

    public void setRequiredVotes(int requiredVotes) {
        this.requiredVotes = requiredVotes;
    }

    public void setRewardText(List<String> rewardText) {
        this.rewardText = rewardText;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
