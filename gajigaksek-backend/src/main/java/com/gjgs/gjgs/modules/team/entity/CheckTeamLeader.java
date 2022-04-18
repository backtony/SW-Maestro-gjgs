package com.gjgs.gjgs.modules.team.entity;

import com.gjgs.gjgs.modules.exception.team.NotTeamLeaderException;
import com.gjgs.gjgs.modules.exception.team.TeamLeaderCanNotExitTeamException;
import com.gjgs.gjgs.modules.member.entity.Member;

public interface CheckTeamLeader {

    default void isLeaderDoThrow(Member leader, Member target) {
        if (leader.getId().equals(target.getId())) {
            throw new TeamLeaderCanNotExitTeamException();
        }
    }

    default void checkNotLeader(Member leader, Member target) {
        if (!isLeader(leader, target)) {
            throw new NotTeamLeaderException();
        }
    }

    private boolean isLeader(Member leader, Member target) {
        if (target.getId() != null) {
            return leader.getId().equals(target.getId());
        }
        return leader.getUsername().equals(target.getUsername());
    }
}
