import {STATUSBAR_HEIGHT} from '@/utils/commonParam';
import React from 'react';
import {StyleSheet, Text, View} from 'react-native';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface EightCategoryViewHeaderProps {
  navigation: any;
  mainCategory: string;
}

export default class EightCategoryViewHeader extends React.Component<
  EightCategoryViewHeaderProps,
  {}
> {
  render() {
    return (
      <View style={this.styles.wrapper}>
        <Button
          icon={<Icon name="chevron-back" size={24} color="#1d1d1f" />}
          buttonStyle={{backgroundColor: 'white'}}
          onPress={() => this.props.navigation.goBack()}
        />
        <Text style={this.styles.text}>{this.props.mainCategory}</Text>
      </View>
    );
  }

  private styles = StyleSheet.create({
    wrapper: {
      marginTop: STATUSBAR_HEIGHT,
      height: 60,
      width: '100%',
      flexDirection: 'row',
      alignItems: 'center',
    },
    text: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 20,
      color: '#1d1d1f',
    },
  });
}
