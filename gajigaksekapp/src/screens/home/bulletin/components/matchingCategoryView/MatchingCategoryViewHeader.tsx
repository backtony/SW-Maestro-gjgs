import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements/dist/buttons/Button';
import Icon from 'react-native-vector-icons/Ionicons';

interface MatchingCategoryViewHeaderProps {
  navigation: any;
  selectedMainCategory: string;
  subZone: any;
}

export default class MatchingCategoryViewHeader extends React.Component<
  MatchingCategoryViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.wrapper}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>대분류 선택</Text>
        </View>
        <Button
          buttonStyle={this.styles.button}
          titleStyle={this.styles.buttonTitle}
          onPress={() => {
            if (!this.props.selectedMainCategory) {
              alert('관심있는 주제를 선택해주세요.');
              return;
            }
            this.props.navigation.navigate('matchingDetail', {
              mainCategory: this.props.selectedMainCategory,
              subZone: this.props.subZone,
            });
          }}
          title={'다음'}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {
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
