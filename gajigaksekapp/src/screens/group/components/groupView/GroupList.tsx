import React from 'react';
import {FlatList, View} from 'react-native';
import CheckBox from '@react-native-community/checkbox';
import GroupItem from './groupItem/GroupItem';
import Category from '@/utils/Category';
import {StyleSheet} from 'react-native';

interface GroupListProps {
  editMode: boolean;
  teamList: any;
  navigation: any;
  checkedTeam: number;
  modalVisible: boolean;
  refreshParent: () => void;
  refreshing: boolean;
  setClickedListTeamId: (teamId: number) => void;
  setModalVisible: (modalVisible: boolean) => void;
  setCheckedTeam: (temId: number) => void;
}

export default class GroupList extends React.Component<GroupListProps, {}> {
  render() {
    return (
      <FlatList
        data={this.props.teamList}
        renderItem={post => {
          const item = post.item;
          return (
            <View style={this.styles.container}>
              {this.props.editMode && (
                <CheckBox
                  disabled={false}
                  value={this.props.checkedTeam === item.teamId}
                  onValueChange={() => this.props.setCheckedTeam(item.teamId)}
                  style={this.styles.checkbox}
                />
              )}
              <GroupItem
                title={item.teamName}
                subCategory={item.categoryList
                  .map(id => `#${Category.getCategory(id)[1]}`)
                  .join(' ')}
                isLeader={item.iamLeader}
                apply={item.applyPeople}
                all={item.maxPeople}
                onPress={() => {
                  this.props.navigation.navigate('groupDashBoard', {
                    teamId: item.teamId,
                  });
                }}
                onPressChat={() => {
                  this.props.navigation.navigate('chatroom', {
                    thread: {
                      teamId: JSON.stringify(item.teamId),
                      teamName: item.teamName,
                      onPressAvatar: (memberId: number) => {
                        this.props.navigation.navigate('profile', {memberId});
                      },
                    },
                  });
                }}
                onPressList={() => {
                  this.props.setClickedListTeamId(item.teamId);
                  this.props.setModalVisible(!this.props.modalVisible);
                }}
              />
            </View>
          );
        }}
        refreshing={this.props.refreshing}
        onRefresh={() => this.props.refreshParent()}
      />
    );
  }

  private styles = StyleSheet.create({
    container: {flexDirection: 'row', alignItems: 'center'},
    checkbox: {marginRight: 10, marginLeft: 16, color: 'black'},
  });
}
