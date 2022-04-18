import React from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';

interface BulletinEditViewMainTextProps {
  text: string;
  setText: (text: string) => void;
}

export default class BulletinEditViewMainText extends React.Component<
  BulletinEditViewMainTextProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>본문</Text>
        <View style={this.styles.wrapper}>
          <TextInput
            style={this.styles.input}
            multiline
            onChangeText={text => this.props.setText(text)}
            value={this.props.text}
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
    wrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 184,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'flex-start',
      padding: 10,
    },
    input: {
      marginTop: 0,
    },
  });
}
