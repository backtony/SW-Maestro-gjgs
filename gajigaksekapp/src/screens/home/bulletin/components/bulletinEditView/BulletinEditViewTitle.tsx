import React from 'react';
import {StyleSheet, Text, TextInput, View} from 'react-native';

interface BulletinEditViewTitleProps {
  title: string;
  setTitle: (title: string) => void;
}

export default class BulletinEditViewTitle extends React.Component<
  BulletinEditViewTitleProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.title}>제목</Text>
        <View style={this.styles.wrapper}>
          <TextInput
            style={this.styles.input}
            onChangeText={text => this.props.setTitle(text)}
            value={this.props.title}
          />
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    title: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 40,
      marginBottom: 10,
    },
    wrapper: {
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
