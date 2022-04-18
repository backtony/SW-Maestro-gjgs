import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface RecommendLectureViewHeaderProps {
  navigation: any;
  title: string;
  onPress: () => void;
}

export default class RecommendLectureViewHeader extends React.Component<
  RecommendLectureViewHeaderProps,
  {}
> {
  render() {
    return (
      <View
        style={[
          {
            marginTop: STATUSBAR_HEIGHT,
          },
          this.styles.header,
        ]}>
        <View style={{flexDirection: 'row', alignItems: 'center'}}>
          <Button
            icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
            buttonStyle={{backgroundColor: 'white'}}
            onPress={() => this.props.navigation.goBack()}
          />
          <Text style={this.styles.text}>{this.props.title}</Text>
        </View>

        <Button
          buttonStyle={{backgroundColor: 'white', marginRight: 10}}
          titleStyle={{color: '#4a4a4c'}}
          title={'필터'}
          onPress={() => this.props.onPress()}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    header: {
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
