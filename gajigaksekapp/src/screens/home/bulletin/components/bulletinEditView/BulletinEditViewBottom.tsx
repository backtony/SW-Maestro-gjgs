import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface BulletinEditViewBottomProps {
  onPressEdit: () => void;
  onPressDelete: () => void;
}

export default class BulletinEditViewBottom extends React.Component<
  BulletinEditViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.onPressEdit();
          }}>
          <Text style={this.styles.buttonText}>수정</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => {
            this.props.onPressDelete();
          }}>
          <Text style={this.styles.buttonText2}>삭제</Text>
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
      flexDirection: 'row',
    },
    button: {
      backgroundColor: '#4f6cff',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      flex: 1,
      marginRight: 5,
    },
    buttonText: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#FFF',
    },
    button2: {
      backgroundColor: '#ececec',
      height: 44,
      alignItems: 'center',
      justifyContent: 'center',
      borderRadius: 6,
      flex: 1,
      marginLeft: 5,
    },
    buttonText2: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#4a4a4c',
    },
  });
}
