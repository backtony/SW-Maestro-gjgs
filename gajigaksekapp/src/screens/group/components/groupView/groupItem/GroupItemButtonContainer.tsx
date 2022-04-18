import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface GroupItemButtonContainerProps {
  onPressChat: () => void;
  onPressList: () => void;
  isLeader: boolean;
}

export default class GroupItemButtonContainer extends React.Component<
  GroupItemButtonContainerProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <TouchableOpacity
          style={this.styles.chatButton}
          onPress={() => {
            this.props.onPressChat();
          }}>
          <View style={this.styles.buttonInnerWrapper}>
            <Icon name="ios-chatbox-ellipses-sharp" size={14} color="#FFF" />
            <Text style={this.styles.chatText}>채팅</Text>
          </View>

          <Icon name="chevron-forward" size={14} color="#FFF" />
        </TouchableOpacity>
        {this.props.isLeader && (
          <TouchableOpacity
            style={this.styles.listButton}
            onPress={() => {
              this.props.onPressList();
            }}>
            <View style={this.styles.buttonInnerWrapper}>
              <Icon name="ios-list" size={14} color="#4f6cff" />
              <Text style={this.styles.listText}>신청자 리스트</Text>
            </View>
            <Icon name="chevron-forward" size={14} color="#4f6cff" />
          </TouchableOpacity>
        )}
      </View>
    );
  }
  private styles = StyleSheet.create({
    container: {
      flexDirection: 'row',
      justifyContent: 'space-between',
      marginTop: 20,
      marginVertical: 10,
    },
    chatButton: {
      height: 40,
      backgroundColor: '#4f6cff',
      borderRadius: 6,
      width: 152,
      alignItems: 'center',
      flexDirection: 'row',
      padding: 12,
      justifyContent: 'space-between',
    },
    chatText: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 15,
      paddingTop: 2,
      color: '#FFF',
      marginLeft: 11,
    },
    listButton: {
      backgroundColor: '#f5f7ff',
      height: 40,
      width: 152,
      borderRadius: 6,
      flexDirection: 'row',
      padding: 12,
      marginLeft: 30,
    },
    listText: {
      fontSize: 14,
      fontFamily: 'NotoSansCJKkr-Regular',
      lineHeight: 15,
      paddingTop: 2,
      color: '#4f6cff',
      marginLeft: 11,
    },
    buttonInnerWrapper: {flexDirection: 'row'},
  });
}
