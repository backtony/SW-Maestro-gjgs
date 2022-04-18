import React from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';

interface BulletinDetailViewTextBlockProps {
  team: any;
}

export default class BulletinDetailViewTextBlock extends React.Component<
  BulletinDetailViewTextBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>본문</Text>
        <View style={this.styles.inputWrapper}>
          <TextInput
            style={this.styles.textInput}
            multiline
            editable={false}
            onChangeText={() => {}}
            value={this.props.team ? this.props.team.bulletinText : ''}
            // value={
            //   '함께 한식을 만들분을 모집해요!\n같이 즐겁게 한식을 만들어봐요!\n저는 매주 즐겁게 클래스를 참여합니다!'
            // }
            placeholder={'본문을 입력해주세요'}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
    inputWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 184,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      padding: 10,
    },
    textInput: {
      marginTop: 0,
    },
  });
}
