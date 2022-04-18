import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

export function Loading() {
  const {container} = styles;
  return (
    <View style={container}>
      <Text>잠시만 기다려주세요...</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    ...StyleSheet.absoluteFillObject,
  },
});

export default Loading;
