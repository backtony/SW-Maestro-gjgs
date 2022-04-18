import React from 'react';
import {ScrollView, StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';
import GroupEditMemberButton from './GroupEditMemberButton';

interface GroupEditLeaderBlockProps {
  memberList: any[];
  clickedMember: string;
  onClickMember: (nickname: string) => void;
  onClickDelegate: () => void;
}

export default class GroupEditLeaderBlock extends React.Component<
  GroupEditLeaderBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <View style={this.styles.titleContainer}>
          <Text style={this.styles.title}>그룹장 위임</Text>
          <Button
            buttonStyle={this.styles.button}
            titleStyle={this.styles.buttonTitle}
            onPress={() => this.props.onClickDelegate()}
            title={'위임'}
          />
        </View>
        <ScrollView horizontal={true}>
          {this.props.memberList &&
            this.props.memberList.map(member => (
              <GroupEditMemberButton
                title={member.nickname}
                onClick={() => this.props.onClickMember(member.nickname)}
                clicked={this.props.clickedMember === member.nickname}
              />
            ))}
        </ScrollView>
      </View>
    );
  }

  private styles = StyleSheet.create({
    titleContainer: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 20,
      marginBottom: 10,
    },
    button: {
      width: 43,
      height: 28,
      borderRadius: 6,
      backgroundColor: 'transparent',
      paddingTop: 8,
      paddingBottom: 5,
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Bold',
    },
  });
}
