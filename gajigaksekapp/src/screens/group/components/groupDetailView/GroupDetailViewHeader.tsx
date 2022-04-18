import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface GroupDetailViewHeaderProps {
  navigation: any;
  team: any;
  refreshParent: () => void;
}

export default class GroupDetailViewHeader extends React.Component<
  GroupDetailViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
          buttonStyle={this.styles.button}
          onPress={() => {
            this.props.navigation.goBack();
          }}
        />
        <Text style={this.styles.text} numberOfLines={1}>
          {this.props.team ? this.props.team.teamName : '그룹1'}
        </Text>
        <View style={this.styles.buttonWrapper}>
          {this.props.team && this.props.team.iamLeader && (
            <Button
              buttonStyle={this.styles.button}
              titleStyle={this.styles.title}
              onPress={() => {
                this.props.navigation.navigate('groupEdit', {
                  team: this.props.team,
                  refreshParent: () => this.props.refreshParent(),
                });
              }}
              title={'편집'}
            />
          )}
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
      paddingHorizontal: 15,
    },
    button: {backgroundColor: 'white'},
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
      width: 200,
    },
    buttonWrapper: {
      flexDirection: 'row',
    },
    title: {color: '#4a4a4c'},
  });
}
