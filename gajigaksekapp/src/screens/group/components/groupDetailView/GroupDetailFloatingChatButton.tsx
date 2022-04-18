import React from 'react';
import {StyleSheet} from 'react-native';
import {TouchableOpacity} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';

interface GroupDetailFloatingChatButtonProps {
  navigation: any;
  teamId: number;
  teamName: string;
}

export default class GroupDetailFloatingChatButton extends React.Component<
  GroupDetailFloatingChatButtonProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.button}
        onPress={() => {
          this.props.navigation.navigate('chatroom', {
            thread: {
              teamId: JSON.stringify(this.props.teamId),
              teamName: this.props.teamName,
            },
          });
        }}>
        <Icon name="md-chatbubble-ellipses" size={20} color="#FFF" />
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    button: {
      width: 44,
      height: 44,
      borderRadius: 44,
      backgroundColor: '#4f6cff',
      position: 'absolute',
      right: 20,
      bottom: 20,
      alignItems: 'center',
      justifyContent: 'center',

      shadowColor: '#4c4f6cff',
      shadowOffset: {
        width: 0,
        height: 4,
      },
      shadowOpacity: 0.29,
      shadowRadius: 4.65,

      elevation: 7,
    },
  });
}
