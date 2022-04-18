import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';
import Icon from 'react-native-vector-icons/Ionicons';

interface MatchingZoneViewHeaderProps {
  navigation: any;
  selectedSubZone: string;
}

export default class MatchingZoneViewHeader extends React.Component<
  MatchingZoneViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.container}>
        <View style={this.styles.header}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>활동지역 선택</Text>
        </View>
        <Button
          buttonStyle={this.styles.button}
          titleStyle={this.styles.buttonTitle}
          onPress={() => {
            if (!this.props.selectedSubZone) {
              alert('활동지역을 선택해주세요.');
              return;
            }
            this.props.navigation.navigate('matchingMainCategory', {
              subZone: this.props.selectedSubZone,
            });
          }}
          title={'다음'}
        />
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
    header: {flexDirection: 'row', alignItems: 'center'},
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
    button: {
      backgroundColor: '#FFF',
      marginRight: 10,
      marginTop: 10,
    },
    buttonTitle: {
      color: '#4f6cff',
      fontSize: 14,
      lineHeight: 16,
      fontFamily: 'NotoSansCJKkr-Regular',
    },
  });
}
