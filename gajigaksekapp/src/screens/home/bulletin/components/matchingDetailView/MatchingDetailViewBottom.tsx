import React from 'react';
import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';

interface MatchingDetailViewBottomProps {
  postMatching: () => void;
  setModalVisible: (visible: boolean) => void;
}

export default class MatchingDetailViewBottom extends React.Component<
  MatchingDetailViewBottomProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          style={this.styles.button}
          onPress={() => {
            this.props.postMatching();
          }}>
          <Text style={this.styles.text}>매칭 시작</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={this.styles.button2}
          onPress={() => {
            this.props.setModalVisible(true);
          }}>
          <Text style={this.styles.text2}>매칭 취소</Text>
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
    text: {
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
    text2: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 16,
      color: '#4a4a4c',
    },
  });
}
