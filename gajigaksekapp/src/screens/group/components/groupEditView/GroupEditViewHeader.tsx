import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet} from 'react-native';
import {Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface GroupEditViewHeaderProps {
  navigation: any;
}

export default class GroupEditViewHeader extends React.Component<
  GroupEditViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
          buttonStyle={{backgroundColor: 'white'}}
          onPress={() => this.props.navigation.goBack()}
        />
        <Text style={this.styles.text}>그룹 편집</Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    container: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'flex-start',
      alignItems: 'center',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}
