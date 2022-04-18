import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TextInput, View} from 'react-native';

interface GroupNameBlockProps {
  title: string;
  setTitle: (title: string) => void;
}

export default class GroupNameBlock extends React.Component<
  GroupNameBlockProps,
  {}
> {
  render() {
    return (
      <View>
        <Text style={this.styles.text}>그룹명</Text>
        <View style={this.styles.textInputWrapper}>
          <TextInput
            style={this.styles.textInput}
            onChangeText={text => {
              this.props.setTitle(text);
            }}
            value={this.props.title}
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
      marginTop: 20,
      marginBottom: 10,
    },
    textInputWrapper: {
      backgroundColor: 'rgba(245, 245, 247, 0.6)',
      height: 44,
      borderRadius: 6,
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    textInput: {
      height: 44,
      width: '70%',
      paddingLeft: 13,
    },
  });
}
