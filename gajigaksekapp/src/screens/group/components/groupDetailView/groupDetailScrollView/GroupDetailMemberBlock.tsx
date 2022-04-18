import GroupInviteModal from '@/screens/modal/GroupInviteModal';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity, View} from 'react-native';
import MemberBlock from '../../../../../components/MemberBlock';

interface GroupDetailMemberBlockProps {
  navigation: any;
  team: any;
  setTeam: (team: any) => void;
}

interface GroupDetailMemberBlockStates {
  memberEdit: boolean;
  modalVisible: boolean;
}

export default class GroupDetailMemberBlock extends React.Component<
  GroupDetailMemberBlockProps,
  GroupDetailMemberBlockStates
> {
  constructor() {
    super();
    this.state = {memberEdit: false, modalVisible: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.headerWrapper}>
          <Text style={this.styles.headerTitle}>인원</Text>
          {this.props.team && this.props.team.iamLeader && (
            <TouchableOpacity
              onPress={() => {
                this.setState({memberEdit: !this.state.memberEdit});
              }}>
              <Text
                style={[
                  this.styles.headerText,
                  this.state.memberEdit ? {color: '#4f6cff'} : {},
                ]}>
                {this.state.memberEdit ? '완료' : '편집'}
              </Text>
            </TouchableOpacity>
          )}
        </View>

        <View>
          {this.props.team
            ? this.props.team.teamMemberList.map(member => (
                <MemberBlock
                  profile={{
                    nickname: member.nickname,
                    age: member.age,
                    info: member.text,
                    gender: member.sex,
                    image: member.imageURL,
                    memberId: member.memberId,
                  }}
                  navigation={this.props.navigation}
                  teamId={this.props.team.teamId}
                  memberEdit={this.state.memberEdit}
                  memberAdd={false}
                  onDelete={() => {
                    const updatedMemberList =
                      this.props.team.teamMemberList.filter(
                        item => item !== member,
                      );
                    let updatedGroup = this.props.team;
                    updatedGroup.teamMemberList = updatedMemberList;
                    this.props.setTeam(updatedGroup);
                  }}
                />
              ))
            : null}
        </View>

        <TouchableOpacity
          style={this.styles.button}
          onPress={() =>
            this.setState({modalVisible: !this.state.modalVisible})
          }>
          <Text style={this.styles.buttonText}>+ 초대</Text>
        </TouchableOpacity>

        <View>
          <GroupInviteModal
            modalVisible={this.state.modalVisible}
            setModalVisible={(visible: boolean) => {
              this.setState({modalVisible: visible});
            }}
            setParentSubZone={(sub: string) => {
              // this.setState({profileSubZone: sub});
            }}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    headerWrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: 42,
      marginBottom: 22,
    },
    headerTitle: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    headerText: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#4a4a4c',
    },
    button: {
      width: '100%',
      backgroundColor: '#4f6cff',
      marginTop: 10,
      borderRadius: 6,
      justifyContent: 'center',
      alignItems: 'center',
      height: 44,
    },
    buttonText: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#FFF',
      paddingTop: 4,
    },
  });
}
