import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface GroupEditViewBottomProps {
  onPress: () => void;
}

export default class GroupEditViewBottom extends React.Component<
  GroupEditViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.wrapper}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => this.props.onPress()}>
          <Text style={this.styles.text}>수정완료</Text>
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {
      height: 60,
      marginHorizontal: 20,
      paddingVertical: 8,
      marginVertical: 8,
      borderTopColor: '#f5f5f7',
      borderTopWidth: 1,
    },
    button: {
      backgroundColor: '#4f6cff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#FFF',
    },
  });
}
