import * as React from 'react';
import {View} from 'react-native';
import GroupApiController from '@/services/apis/GroupApiController';
import GroupDetailViewHeader from './components/groupDetailView/GroupDetailViewHeader';
import GroupDetailScrollView from './components/groupDetailView/groupDetailScrollView/GroupDetailScrollView';
import GroupDetailFloatingChatButton from './components/groupDetailView/GroupDetailFloatingChatButton';

interface GroupDetailViewStates {
  team: any;
}

export default class GroupDetailView extends React.Component<
  {},
  GroupDetailViewStates
> {
  constructor() {
    super();
    this.state = {
      team: null,
    };
  }
  componentDidMount() {
    this.getGroupDash(this.props.route.params.teamId);
  }

  private async getGroupDash(teamId: number) {
    try {
      const res = await GroupApiController.getTeamDash({teamId: teamId});
      let groupDashData = res?.data;
      groupDashData.teamMemberList = [
        groupDashData.teamsLeader,
        ...groupDashData.teamMemberList,
      ];
      groupDashData.teamId = teamId;
      this.setState({team: groupDashData});
    } catch (e) {
      console.error(e);
    }
  }

  render() {
    return (
      <View style={{flex: 1, backgroundColor: '#FFF'}}>
        <GroupDetailViewHeader
          navigation={this.props.navigation}
          team={this.state.team}
          refreshParent={() => this.getGroupDash(this.state.team.teamId)}
        />
        <GroupDetailScrollView
          navigation={this.props.navigation}
          team={this.state.team}
          setTeam={(team: any) => this.setState({team: team})}
        />
        {this.state.team && (
          <GroupDetailFloatingChatButton
            navigation={this.props.navigation}
            teamId={this.state.team.teamId}
            teamName={this.state.team.teamName}
          />
        )}
      </View>
    );
  }
}
