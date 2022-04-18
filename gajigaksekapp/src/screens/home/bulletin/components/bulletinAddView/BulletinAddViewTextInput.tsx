import React from 'react';
import {Text, TextInput, View} from 'react-native';

interface BulletinAddViewTextInputProps {
  text: string;
  setText: (text: string) => void;
}

export default class BulletinAddViewTextInput extends React.Component<
  BulletinAddViewTextInputProps,
  {}
> {
  render() {
    return (
      <View>
        <Text
          style={{
            fontSize: 16,
            lineHeight: 18,
            fontFamily: 'NotoSansCJKkr-Bold',
            marginTop: 40,
            marginBottom: 10,
          }}>
          본문
        </Text>
        <View
          style={{
            // marginHorizontal: 20,
            backgroundColor: 'rgba(245, 245, 247, 0.6)',
            height: 184,
            borderRadius: 6,
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'flex-start',
            padding: 10,
          }}>
          <TextInput
            style={{
              maxLength: 150,
              // marginHorizontal: 13,
              marginTop: 0,
              numberOfLines: 5,
            }}
            multiline
            onChangeText={text => this.props.setText(text)}
            value={this.props.text}
            placeholder={'본문을 입력해주세요'}
          />
        </View>
      </View>
    );
  }
}
