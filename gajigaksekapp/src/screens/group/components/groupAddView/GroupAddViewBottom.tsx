import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity, View} from 'react-native';

interface GroupAddViewBottomProps {
  onPress: () => void;
}

export default class GroupAddViewBottom extends React.Component<
  GroupAddViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.onPress();
          }}>
          <Text style={this.styles.text}>그룹 생성하기</Text>
        </TouchableOpacity>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
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
