import GroupApiController from '@/services/apis/GroupApiController';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Alert, Text, TouchableOpacity} from 'react-native';

interface GroupExitButtonProps {
  navigation: any;
  checkedGroup: number;
  refreshParent: () => void;
  checkLeader: (teamId: number) => void;
}

export default class GroupExitButton extends React.Component<
  GroupExitButtonProps,
  {}
> {
  private async deleteTeamWithLeader(teamId: number) {
    try {
      await GroupApiController.deleteTeamWithLeader({
        teamId: teamId,
      });
      Alert.alert('팀 삭제가 완료되었습니다.');
    } catch (e) {
      Alert.alert('팀 삭제를 다시 시도해주세요.');
      console.error(e);
    }
  }

  private async deleteTeamWithMember(teamId: number) {
    try {
      await GroupApiController.deleteTeamWithMember({
        teamId: teamId,
      });
      Alert.alert('팀 나가기가 완료되었습니다.');
    } catch (e) {
      Alert.alert('팀 나가기를 다시 시도해주세요.');
      console.error(e);
    }
  }
  render() {
    return (
      <TouchableOpacity
        style={this.styles.container}
        onLongPress={() => {
          this.props.navigation.navigate('chatroom', {
            thread: {
              teamId: JSON.stringify(6),
              teamName: '테스트6',
            },
          });
        }}
        onPress={async () => {
          if (
            this.props.checkedGroup &&
            this.props.checkLeader(this.props.checkedGroup)
          ) {
            await this.deleteTeamWithLeader(this.props.checkedGroup);
            this.props.refreshParent();
          } else if (this.props.checkedGroup) {
            await this.deleteTeamWithMember(this.props.checkedGroup);
            this.props.refreshParent();
          }
        }}>
        <Text style={this.styles.text}>나가기</Text>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    container: {
      backgroundColor: '#ff4f4f',
      width: 160,
      height: 44,
      borderRadius: 22,
      alignItems: 'center',
      justifyContent: 'center',
      position: 'absolute',
      bottom: 30,
      alignSelf: 'center',

      shadowColor: '#000',
      shadowOffset: {
        width: 0,
        height: 3,
      },
      shadowOpacity: 0.27,
      shadowRadius: 4.65,

      elevation: 6,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      lineHeight: 18,
      paddingTop: 5,
      color: '#FFF',
    },
  });
}
