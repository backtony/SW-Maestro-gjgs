import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';
import Icon from 'react-native-vector-icons/Ionicons';

interface MatchingDetailViewHeaderProps {
  navigation: any;
}

export default class MatchingDetailViewHeader extends React.Component<
  MatchingDetailViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>세부사항 선택</Text>
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
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}
