import * as React from 'react';
import {View, StyleSheet} from 'react-native';
import GroupApiController from '../../services/apis/GroupApiController';
import UserDC from '../../services/login/UserDC';
import GroupApplyListModal from '../modal/GroupApplyListModal';
import GroupViewHeader from './components/groupView/GroupViewHeader';
import GroupList from './components/groupView/GroupList';
import FloatingAddButton from '@/components/FloatingAddButton';
import GroupExitButton from './components/groupView/GroupExitButton';

interface GroupViewStates {
  modalVisible: boolean;
  editMode: boolean;
  checkedGroup: number;
  groupList: any;
  refreshing: boolean;
  clickedListTeamId: number;
}

class GroupView extends React.Component<{}, GroupViewStates> {
  constructor() {
    super();
    this.state = {
      modalVisible: false,
      editMode: false,
      groupList: [],
      refreshing: true,
    };
  }

  componentDidMount() {
    this.getGroupList();
  }

  private async getGroupList() {
    if (UserDC.isLogout()) {
      this.setState({refreshing: false, groupList: []});
      return;
    }
    try {
      const res = await GroupApiController.getTeamList({});
      const myTeamList = res?.data.myTeamList;

      this.setState({groupList: myTeamList, refreshing: false});
    } catch (e) {
      console.error(e);
    }
  }

  private checkLeader(teamId: number) {
    const team = this.state.groupList.filter(group => group.teamId === teamId);
    return team.length > 0 && team[0].iamLeader;
  }

  render() {
    return (
      <View style={this.styles.container}>
        <GroupViewHeader
          editMode={this.state.editMode}
          setEditMode={(editMode: boolean) => this.setState({editMode})}
        />
        <GroupList
          editMode={this.state.editMode}
          teamList={this.state.groupList}
          navigation={this.props.navigation}
          checkedTeam={this.state.checkedGroup}
          modalVisible={this.state.modalVisible}
          refreshParent={() => this.getGroupList()}
          refreshing={this.state.refreshing}
          setClickedListTeamId={(teamId: number) =>
            this.setState({clickedListTeamId: teamId})
          }
          setModalVisible={(modalVisible: boolean) =>
            this.setState({modalVisible})
          }
          setCheckedTeam={(teamId: number) =>
            this.setState({checkedGroup: teamId})
          }
        />
        {!this.state.editMode && (
          <FloatingAddButton
            navigation={this.props.navigation}
            nextPageName={'groupAdd'}
            refreshParent={() => {
              this.getGroupList();
            }}
          />
        )}
        {this.state.editMode && (
          <GroupExitButton
            navigation={this.props.navigation}
            checkedGroup={this.state.checkedGroup}
            refreshParent={() => this.getGroupList()}
            checkLeader={(teamId: number) => this.checkLeader(teamId)}
          />
        )}
        <View>
          <GroupApplyListModal
            modalVisible={this.state.modalVisible}
            teamId={this.state.clickedListTeamId}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {flex: 1, backgroundColor: '#FFF'},
  });
}

export default GroupView;
