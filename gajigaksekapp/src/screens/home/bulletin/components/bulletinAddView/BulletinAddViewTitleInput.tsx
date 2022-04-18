import React from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';

interface BulletinAddViewTitleInputProps {
  title: string;
  setTitle: (title: string) => void;
}

export default class BulletinAddViewTitleInput extends React.Component<
  BulletinAddViewTitleInputProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>제목</Text>
        <View style={this.styles.inputWrapper}>
          <TextInput
            style={this.styles.input}
            onChangeText={text => this.props.setTitle(text)}
            value={this.props.title}
            placeholder={'제목을 입력해주세요.'}
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
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    input: {
      height: 44,
      width: '70%',
      paddingLeft: 13,
    },
  });
}
