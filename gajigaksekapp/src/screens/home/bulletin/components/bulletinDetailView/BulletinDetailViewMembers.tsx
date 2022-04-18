import MemberBlock from '@/components/MemberBlock';
import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface BulletinDetailViewMembersProps {
  team: any;
  navigation: any;
}

interface BulletinDetailViewMembersStates {
  memberEdit: boolean;
}

export default class BulletinDetailViewMembers extends React.Component<
  BulletinDetailViewMembersProps,
  BulletinDetailViewMembersStates
> {
  constructor(props: any) {
    super(props);
    this.state = {memberEdit: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.titleWrapper}>
          <Text style={this.styles.title}>참여자</Text>
          {this.props.team && this.props.team.bulletinsTeam.iamLeader && (
            <TouchableOpacity
              onPress={() => {
                this.setState({memberEdit: !this.state.memberEdit});
              }}>
              {/* <Text
                style={[
                  this.styles.text,
                  this.state.memberEdit ? {color: '#4f6cff'} : {},
                ]}>
                {this.state.memberEdit ? '완료' : '편집'}
              </Text> */}
            </TouchableOpacity>
          )}
        </View>

        <View>
          {this.props.team
            ? this.props.team.bulletinsTeam.members
                .slice(0, this.props.team.bulletinsTeam.members.length - 1)
                .map(member => (
                  <MemberBlock
                    key={member.id}
                    profile={{
                      nickname: member.nickname,
                      age: member.age,
                      info: member.text,
                      gender: member.sex,
                      image: member.imageUrl,
                      memberId: member.memberId,
                    }}
                    navigation={this.props.navigation}
                    teamId={this.props.team.bulletinsTeam.teamId}
                    memberEdit={this.state.memberEdit}
                    memberAdd={false}
                    onDelete={() => {}}
                  />
                ))
            : null}
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    titleWrapper: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      marginTop: 42,
      marginBottom: 22,
    },
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
    text: {
      fontSize: 12,
      lineHeight: 14,
      fontFamily: 'NotoSansCJKkr-Bold',
      color: '#4a4a4c',
    },
  });
}
