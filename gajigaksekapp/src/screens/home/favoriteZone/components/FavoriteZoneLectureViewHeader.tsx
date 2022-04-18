import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface FavoriteZoneLectureViewHeaderProps {
  onPressBack: () => void;
  onPressFilter: () => void;
}

export default class FavoriteZoneLectureViewHeader extends React.Component<
  FavoriteZoneLectureViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.header}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.onPressBack()}
          />
          <Text style={this.styles.text}>관심지역 클래스</Text>
        </View>

        <Button
          buttonStyle={{backgroundColor: 'white', marginRight: 10}}
          titleStyle={{color: '#4a4a4c'}}
          title={'필터'}
          onPress={() => this.props.onPressFilter()}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    header: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    text: {
      // width: 93,
      // height: 20,
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}
